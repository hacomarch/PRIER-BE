package cocodas.prier.user.kakao.jwt.token;

import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import org.springframework.data.redis.core.RedisHash;

@RedisHash(value = "refreshToken", timeToLive = 14 * 24 * 60 * 60 * 1000L)
@AllArgsConstructor
@Getter
@Builder
public class Token {

    @Id
    private Long id;

    private String refreshToken;

    public static Token of(Long id, String refreshToken) {
        return Token.builder()
                .id(id)
                .refreshToken(refreshToken)
                .build();
    }
}
