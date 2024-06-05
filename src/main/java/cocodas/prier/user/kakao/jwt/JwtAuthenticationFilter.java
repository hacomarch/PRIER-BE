package cocodas.prier.user.kakao.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

//모든 요청에 대해 실행되며 요청의 Authorization 헤더에서 jwt 추출, 검증해 사용자 인증
@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtTokenProvider jwtTokenProvider; //jwt 토큰 생성, 검증

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        try {
            final String token = getJwtFromRequest(request);
            if (jwtTokenProvider.validateToken(token) == JwtValidationType.VALID_JWT) {
                Long userId = jwtTokenProvider.getUserIdFromJwt(token);
                String kakaoAccessTokenFromJwt = jwtTokenProvider.getKakaoAccessTokenFromJwt(token);
                //userId를 principal(주체)로 설정
                UserAuthentication authentication = new UserAuthentication(userId.toString(), null, null, kakaoAccessTokenFromJwt);
                //IP 주소, 세션 ID 같은 요청 관련 정보를 포함하는 객체를 생성해서 추가적인 인증 정보 설정
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                //spring security의 인증 컨텍스트에 설정
                //현재 스레드의 보안 컨텍스트에 authentication을 설정해 이후의 요청 처리 과정에서 현재 사용자가 인증된 사용자로 간주됨.
                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        } catch (Exception exception) {
            try {
                throw new Exception();
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        filterChain.doFilter(request, response); //다음 필터로 요청 전달
    }

    //요청의 Authorization 헤더에서 Bearer 토큰 찾아서 반환
    private String getJwtFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring("Bearer ".length()); //Bearer 이후의 문자열을 잘라내서 반환, 실제 jwt
        }
        return null;
    }
}
