package cocodas.prier.point.kakao;

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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

    @Value("${kakao.admin-key}")
    private String adminKey;

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

        //todo : 사용자 포인트 올려주는거 추가해야함

        return payApproveResDto;
    }
}
