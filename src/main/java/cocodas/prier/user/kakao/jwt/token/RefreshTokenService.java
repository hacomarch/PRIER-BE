package cocodas.prier.user.kakao.jwt.token;

import com.amazonaws.services.kms.model.NotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class RefreshTokenService {
    private final TokenRepository tokenRepository;

    @Transactional
    public void saveRefreshToken(Long userId, String refreshToken) {
        Token token = Token.of(userId, refreshToken);

        log.info("Saved Refresh Token: {}", token.getRefreshToken());
        log.info("Saved Refresh Id: {}", token.getUserId());

        tokenRepository.save(token);
    }

    @Transactional
    public void deleteRefreshToken(Long userId) {
        Token token = tokenRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("Refresh Token Not Found"));
        tokenRepository.delete(token);
    }

    @Transactional
    public Long findIdByRefreshToken(final String refreshToken) {
        Token token = tokenRepository.findByRefreshToken(refreshToken)
                .orElseThrow(
                        () -> new RuntimeException("RefreshToken 을 찾을 수 없습니다.")
                );
        Long userId = token.getUserId();
        log.info("RefreshTokenService userId : {}", userId);
        return userId;
    }
}
