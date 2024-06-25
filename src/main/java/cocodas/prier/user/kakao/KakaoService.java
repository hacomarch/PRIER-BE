package cocodas.prier.user.kakao;

import cocodas.prier.project.feedback.response.ResponseService;
import cocodas.prier.quest.Quest;
import cocodas.prier.quest.QuestService;
import cocodas.prier.user.UserRepository;
import cocodas.prier.user.UserService;
import cocodas.prier.user.Users;
import cocodas.prier.user.dto.NotificationDto;
import cocodas.prier.user.dto.response.KakaoTokenResponseDto;
import cocodas.prier.user.dto.response.KakaoUserInfoResponseDto;
import cocodas.prier.user.kakao.jwt.JwtTokenProvider;
import cocodas.prier.user.dto.response.LoginSuccessResponse;
import cocodas.prier.user.kakao.jwt.UserAuthentication;
import cocodas.prier.user.response.ProfileImgDto;
import io.netty.handler.codec.http.HttpHeaderValues;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
public class KakaoService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final QuestService questService;
    private final UserService userService;
    private final ResponseService responseService;

    @Value("${kakao.client_id}")
    private String client_id;

    @Value("${kakao.redirect_uri}")
    private String redirect_uri;

    @Value("${kakao.client_secret}")
    private String client_secret;
    private final String KAUTH_TOKEN_URL_HOST = "https://kauth.kakao.com";
    private final String KAUTH_USER_URL_HOST = "https://kapi.kakao.com";

    @Transactional
    public LoginSuccessResponse kakaoLogin(String code) {
        String accessToken = getAccessToken(code);
        KakaoUserInfoResponseDto userInfo = getUserInfo(accessToken);
        Long userId = getUserByEmail(userInfo.getKakaoAccount().email).getUserId();
        return getTokenByUserId(userId, accessToken);
    }

    public String getAccessToken(String code) {
        KakaoTokenResponseDto kakaoTokenResponseDto = WebClient.create(KAUTH_TOKEN_URL_HOST).post()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/oauth/token")
                        .queryParam("grant_type", "authorization_code")
                        .queryParam("client_id", client_id)
                        .queryParam("redirect_uri", redirect_uri)
                        .queryParam("code", code)
                        .queryParam("client_secret", client_secret)
                        .build(true)
                ).header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                .bodyToMono(KakaoTokenResponseDto.class)
                .block();

        return kakaoTokenResponseDto.getAccessToken();
    }

    public KakaoUserInfoResponseDto getUserInfo(String accessToken) {
        KakaoUserInfoResponseDto userInfo = WebClient.create(KAUTH_USER_URL_HOST).get()
                .uri(uriBuilder -> uriBuilder
                        .scheme("https")
                        .path("/v2/user/me")
                        .build(true))
                .header(HttpHeaders.AUTHORIZATION, "Bearer " + accessToken)
                .header(HttpHeaders.CONTENT_TYPE, HttpHeaderValues.APPLICATION_X_WWW_FORM_URLENCODED.toString())
                .retrieve()
                .bodyToMono(KakaoUserInfoResponseDto.class)
                .block();

        if (!isDuplicateEmail(userInfo.getKakaoAccount().email)) { //가입하지 않은 사용자라면
            Users user = Users.builder()
                    .email(userInfo.getKakaoAccount().email)
                    .nickname(userInfo.getKakaoAccount().getProfile().nickName)
                    .build();
            userRepository.save(user);
            questService.createQuest(user);
        } else { //가입한 사용자라면
            Users users = userRepository.findByEmail(userInfo.getKakaoAccount().email)
                    .orElseThrow(() -> new RuntimeException("User Not Found"));

            Quest todayQuest = users.getQuests()
                    .stream()
                    .filter(quest -> quest.getCreatedAt().equals(LocalDate.now()))
                    .findFirst()
                    .orElse(null);

            if (todayQuest == null) {
                questService.createQuest(users);
            }
        }

        return userInfo;
    }

    public Users getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("이메일이 없습니다."));
    }

    public LoginSuccessResponse getTokenByUserId(Long userId, String kakaoAccessToken) {
        UserAuthentication userAuthentication = new UserAuthentication(
                userId,
                null,
                null,
                kakaoAccessToken);

        String accessToken = jwtTokenProvider.generateToken(userAuthentication);

        ProfileImgDto profile = userService.getProfile(userId);

        NotificationDto notificationDto = responseService.noticeAmount(accessToken);

        Users users = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Not Found User"));
        users.updateLastLoginAt(LocalDateTime.now());

        return new LoginSuccessResponse(userId, accessToken, kakaoAccessToken, profile, notificationDto);
    }

    private boolean isDuplicateEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}