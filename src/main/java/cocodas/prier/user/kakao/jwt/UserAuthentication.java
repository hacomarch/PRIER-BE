package cocodas.prier.user.kakao.jwt;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;

import java.util.Collection;

//사용자 인증 객체를 나타내고, 인증된 사용자에 대한 정보 포함. 주로 사용자 인증 및 권한 부여에 사용
public class UserAuthentication extends UsernamePasswordAuthenticationToken {
    private final String kakaoAccessToken;

    public UserAuthentication(Object principal, Object credentials, Collection<? extends GrantedAuthority> authorities, String kakaoAccessToken) {
        super(principal, credentials, authorities);
        this.kakaoAccessToken = kakaoAccessToken;
    }

    public String getKakaoAccessToken() {
        return kakaoAccessToken;
    }
}
