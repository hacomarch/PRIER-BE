package cocodas.prier.user.kakao.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

@Component
@RequiredArgsConstructor
public class JwtTokenProvider {

    private static final String USER_ID = "userId";
    private static final Long TOKEN_EXPIRATION_TIME = 60 * 60 * 1000L; //1시간
    private static final Long REFRESH_TOKEN_EXPIRATION_TIME = 14 * 24 * 60 * 60 * 1000L; // 2주

    @Value("${jwt.secret}")
    private String JWT_SECRET; //jwt 서명을 위한 비밀키

    @PostConstruct
    protected void init() {
        //UTF-8 인코딩 된 바이트 배열 -> Base64 인코딩 된 문자열
        //서명 및 검증 과정에서 비밀 키를 안전하게 관리하기 위해 수행
        JWT_SECRET = Base64.getEncoder()
                .encodeToString(JWT_SECRET.getBytes(StandardCharsets.UTF_8));
    }

    //토큰 생성
    public String generateToken(Authentication authentication) {
        final Date now = new Date();
        final Claims claims = Jwts.claims()
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + TOKEN_EXPIRATION_TIME));

        claims.put(USER_ID, authentication.getPrincipal()); //userId 추가
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(claims)
                .signWith(getSigningKey())
                .compact();
    }

    public String issueRefreshToken(Authentication authentication) {
        final Date now = new Date();
        final Claims claims = Jwts.claims()
                .setIssuedAt(now)
                .setExpiration(new Date(now.getTime() + REFRESH_TOKEN_EXPIRATION_TIME));

        claims.put(USER_ID, authentication.getPrincipal());
        return Jwts.builder()
                .setHeaderParam(Header.TYPE, Header.JWT_TYPE)
                .setClaims(claims)
                .signWith(getSigningKey())
                .compact();
    }

    //서명에 사용할 키 생성
    private SecretKey getSigningKey() {
        String encodedKey = Base64.getEncoder().encodeToString(JWT_SECRET.getBytes());
        return Keys.hmacShaKeyFor(encodedKey.getBytes()); //인코딩된 키를 HMAC-SHA 키로 변환
    }

    //토큰 유효성 검사
    public JwtValidationType validateToken(String token) {
        try {
            final Claims claims = getBody(token); //클레임에서 바디 추출
            return JwtValidationType.VALID_JWT;
        } catch (MalformedJwtException e) {
            return JwtValidationType.INVALID_JWT_TOKEN;
        } catch (ExpiredJwtException e) {
            return JwtValidationType.EXPIRED_JWT_TOKEN;
        } catch (UnsupportedJwtException e) {
            return JwtValidationType.UNSUPPORTED_JWT_TOKEN;
        } catch (IllegalArgumentException e) {
            return JwtValidationType.EMPTY_JWT;
        }
    }

    //jwt 토큰에서 클레임 추출
    private Claims getBody(final String token) {
        return Jwts.parserBuilder() //jwt 문자열을 파싱하기 위한 빌더 생성
                .setSigningKey(getSigningKey()) //서명 키 설정
                .build()
                .parseClaimsJws(token) //토큰 파싱, 서명 검증
                .getBody(); //클레임 반환
    }

    public Long getUserIdFromJwt(String token) {
        Claims claims = getBody(token);
        return Long.valueOf(claims.get(USER_ID).toString()); //userId를 Long으로 변환해 반환
    }
}
