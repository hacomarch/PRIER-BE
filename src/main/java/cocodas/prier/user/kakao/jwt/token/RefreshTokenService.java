package cocodas.prier.user.kakao.jwt.token;

import com.amazonaws.services.kms.model.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final RedisTemplate<String, Object> redisTemplate;
    private final TokenRepository tokenRepository;

    @Transactional
    public void saveRefreshToken(Long userId, String refreshToken) {
        tokenRepository.save(Token.of(userId, refreshToken));
    }

    @Transactional
    public void deleteRefreshToken(Long userId) {
        Token token = tokenRepository.findById(userId)
                .orElseThrow(() -> new NotFoundException("Refresh Token Not Found"));
        tokenRepository.delete(token);
    }

    public Long findIdByRefreshToken(String refreshToken) {
        return tokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new NotFoundException("Refresh Token Not Found"))
                .getId();
    }
}
