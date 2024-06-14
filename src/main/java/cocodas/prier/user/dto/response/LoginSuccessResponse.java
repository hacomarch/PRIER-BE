package cocodas.prier.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginSuccessResponse {
    private Long userId;
    private String accessToken;
    private String kakaoAccessToken;
}
