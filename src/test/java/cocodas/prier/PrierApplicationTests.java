package cocodas.prier;

import cocodas.prier.user.kakao.jwt.token.RefreshTokenService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class PrierApplicationTests {
	private final RefreshTokenService refreshTokenService;

	@Autowired
	public PrierApplicationTests(RefreshTokenService refreshTokenService) {
		this.refreshTokenService = refreshTokenService;
	}

	@Test
	public void testTokenSaveAndFind() {
		Long userId = 1L;
		String refreshToken = "sample-refresh-token";

		// Save the token
		refreshTokenService.saveRefreshToken(userId, refreshToken);

		// Find the token
		Long foundUserId = refreshTokenService.findIdByRefreshToken(refreshToken);
		System.out.println("Found UserId: " + foundUserId);

		// Assertions to check if the saved and found user IDs match
		assertEquals(userId, foundUserId);
	}
}

