package cocodas.prier.user.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginSuccessResponse {
    private String accessToken;
    private Long userId;
    private String email;
    private String nickname;
}
