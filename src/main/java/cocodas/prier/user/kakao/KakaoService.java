package cocodas.prier.user.kakao;

import cocodas.prier.user.UserRepository;
import cocodas.prier.user.Users;
import cocodas.prier.user.dto.response.KakaoTokenResponseDto;
import cocodas.prier.user.dto.response.KakaoUserInfoResponseDto;
import cocodas.prier.user.kakao.jwt.token.RefreshTokenService;
import io.netty.handler.codec.http.HttpHeaderValues;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
@RequiredArgsConstructor
@Slf4j
public class KakaoService {
    private final UserRepository userRepository;
    private final RefreshTokenService refreshTokenService;

    @Value("${kakao.client_id}")
    private String client_id;

    @Value(("${kakao.redirect_uri}"))
    private String redirect_uri;

    @Value("${kakao.client_secret}")
    private String client_secret;
    private final String KAUTH_TOKEN_URL_HOST = "https://kauth.kakao.com";
    private final String KAUTH_USER_URL_HOST = "https://kapi.kakao.com";

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

        if (isDuplicateEmail(userInfo.getKakaoAccount().email)) {
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

    private boolean isDuplicateEmail(String email) {
        Users users = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("이메일이 없습니다."));

        return users == null;
    }


}
