package cocodas.prier.user.kakao;

import cocodas.prier.user.UserRepository;
import cocodas.prier.user.Users;
import cocodas.prier.user.dto.response.KakaoTokenResponseDto;
import cocodas.prier.user.dto.response.KakaoUserInfoResponseDto;
import cocodas.prier.user.kakao.jwt.JwtTokenProvider;
import cocodas.prier.user.dto.response.LoginSuccessResponse;
import cocodas.prier.user.kakao.jwt.UserAuthentication;
import io.netty.handler.codec.http.HttpHeaderValues;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
public class KakaoService {
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Value("${kakao.client_id}")
    private String client_id;

    @Value("${kakao.redirect_uri}")
    private String redirect_uri;

    @Value("${kakao.client_secret}")
    private String client_secret;
    private final String KAUTH_TOKEN_URL_HOST = "https://kauth.kakao.com";
    private final String KAUTH_USER_URL_HOST = "https://kapi.kakao.com";

    public LoginSuccessResponse kakaoLogin(String code) {
        String accessToken = getAccessToken(code);
        KakaoUserInfoResponseDto userInfo = getUserInfo(accessToken);
        Long userId = getUserByEmail(userInfo.getKakaoAccount().email).getUserId();
        return getTokenByUserId(userId);
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

        if (!isDuplicateEmail(userInfo.getKakaoAccount().email)) {
            Users user = Users.builder()
                    .email(userInfo.getKakaoAccount().email)
                    .nickname(userInfo.getKakaoAccount().getProfile().nickName)
                    .build();
            userRepository.save(user);
        }

        return userInfo;
    }

    public Users getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("이메일이 없습니다."));
    }

    public LoginSuccessResponse getTokenByUserId(Long userId) {
        UserAuthentication userAuthentication = new UserAuthentication(userId, null, null);

        String accessToken = jwtTokenProvider.generateToken(userAuthentication);

        Users user = getUserById(userId);
        return new LoginSuccessResponse(
                accessToken,
                userId,
                user.getEmail(),
                user.getNickname());
    }

    private Users getUserById(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User Not Found"));
    }

    private boolean isDuplicateEmail(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
