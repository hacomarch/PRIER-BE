package cocodas.prier.user.kakao.jwt.token;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final TokenRepository tokenRepository;

    @Transactional
    public void saveRefreshToken(Long userId, String refreshToken) {
        Token token = Token.of(userId, refreshToken);
        tokenRepository.save(token);
    }

    @Transactional
    public void deleteRefreshToken(Long userId) {
        Token token = tokenRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Refresh Token Not Found"));
        tokenRepository.delete(token);
    }

    public Long findUserIdByRefreshToken(String refreshToken) {
        return tokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(() -> new RuntimeException("Refresh Token Not Found"))
                .getUserId();
    }
}
