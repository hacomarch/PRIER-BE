package cocodas.prier.point.kakao;

import cocodas.prier.point.kakao.response.KakaoCancelResponse;
import cocodas.prier.point.pointTransaction.PointTransactionService;
import cocodas.prier.point.pointTransaction.TransactionType;
import cocodas.prier.point.kakao.request.MakePayRequest;
import cocodas.prier.point.kakao.request.PayRequest;
import cocodas.prier.point.kakao.response.PayApproveResDto;
import cocodas.prier.point.kakao.response.PayReadyResDto;
import cocodas.prier.user.UserRepository;
import cocodas.prier.user.Users;
import cocodas.prier.user.kakao.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
public class KakaoPayService {

    private final MakePayRequest makePayRequest;
    private final UserRepository userRepository;
    private final KakaoPayRepository kakaoPayRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final PointTransactionService pointTransactionService;

    @Value("${kakao.admin-key}")
    private String adminKey;

    @Value("${cid}")
    private String cid;

    private Users getUsersByToken(String token) {
        Long userId = jwtTokenProvider.getUserIdFromJwt(token);
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 유저"));
        log.info("유저 정보: " + user.getNickname());
        return user;
    }

    @Transactional
    public PayReadyResDto getRedirectUrl(PayInfoDto payInfoDto, String token) throws Exception {

        Users user = getUsersByToken(token);
        Long id = user.getUserId();

        HttpHeaders headers = new HttpHeaders();

        String auth = "KakaoAK " + adminKey;
        headers.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");
        headers.set("Authorization", auth);

        PayRequest payRequest = makePayRequest.getReadyRequest(id, payInfoDto);

        HttpEntity<MultiValueMap<String, String>> urlRequest = new HttpEntity<>(payRequest.getMap(), headers);

        RestTemplate restTemplate = new RestTemplate();
        PayReadyResDto payReadyResDto = restTemplate.postForObject(payRequest.getUrl(), urlRequest, PayReadyResDto.class);

        KakaoPay kakaoPay = KakaoPay.builder()
                .tid(payReadyResDto.getTid())
                .users(user)
                .build();

        kakaoPayRepository.save(kakaoPay);

        return payReadyResDto;
    }

    @Transactional
    public PayApproveResDto getApprove(String pgToken, Long id) throws Exception {

        Users user = userRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 유저"));
        log.info("유저 정보: " + user.getNickname());

        List<KakaoPay> kakaoPay = kakaoPayRepository.findKakaoPayByUsers(user);

        String tid = kakaoPay.get(kakaoPay.size() - 1).getTid();

        HttpHeaders headers = new HttpHeaders();
        String auth = "KakaoAK " + adminKey;

        headers.set("Content-type","application/x-www-form-urlencoded;charset=utf-8");
        headers.set("Authorization",auth);

        PayRequest payRequest = makePayRequest.getApproveRequest(tid, id, pgToken);

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(payRequest.getMap(), headers);

        RestTemplate restTemplate = new RestTemplate();
        PayApproveResDto payApproveResDto = restTemplate.postForObject(payRequest.getUrl(), requestEntity, PayApproveResDto.class);

        log.info("결과: " + payApproveResDto.toString());

        pointTransactionService.increasePoints(user, payApproveResDto.getAmount().getTotal() / 10, TransactionType.POINT_CHARGE, tid);

        return payApproveResDto;
    }

    public KakaoCancelResponse kakaoCancel(String tid, int cancelAmount, int cancelTaxFreeAmount, String token) {
        Users user = getUsersByToken(token);  // 토큰을 사용하여 사용자 검증

        // 카카오페이 요청
        MultiValueMap<String, String> parameters = new LinkedMultiValueMap<>();
        parameters.add("cid", cid);
        parameters.add("tid", tid); // 환불할 결제 고유 번호
        parameters.add("cancel_amount", String.valueOf(cancelAmount)); // 환불 금액
        parameters.add("cancel_tax_free_amount", String.valueOf(cancelTaxFreeAmount)); // 환불 비과세 금액

        // 파라미터, 헤더
        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(parameters, getHeaders());

        // 외부에 보낼 url
        RestTemplate restTemplate = new RestTemplate();

        try {
            ResponseEntity<KakaoCancelResponse> responseEntity = restTemplate.postForEntity(
                    "https://kapi.kakao.com/v1/payment/cancel",
                    requestEntity,
                    KakaoCancelResponse.class);

            if (responseEntity.getStatusCode() == HttpStatus.OK) {
                log.info("Payment cancellation successful: " + responseEntity.getBody());
                pointTransactionService.decreasePoints(user, responseEntity.getBody().getAmount().getTotal() / 10, TransactionType.REFUND);
                return responseEntity.getBody();
            } else {
                log.error("Failed to cancel payment: " + responseEntity.getStatusCode());
                throw new RuntimeException("Failed to cancel payment: " + responseEntity.getStatusCode());
            }
        } catch (Exception e) {
            log.error("Exception during payment cancellation", e);
            throw e;
        }
    }




    private HttpHeaders getHeaders() {
        HttpHeaders httpHeaders = new HttpHeaders();

        String auth = "KakaoAK " + adminKey;

        httpHeaders.set("Authorization", auth);
        httpHeaders.set("Content-type", "application/x-www-form-urlencoded;charset=utf-8");

        return httpHeaders;
    }
}
