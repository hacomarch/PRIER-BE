package cocodas.prier.user.dto.response;

import cocodas.prier.project.feedback.response.ResponseService;
import cocodas.prier.user.dto.NotificationDto;
import cocodas.prier.user.response.ProfileImgDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginSuccessResponse {
    private Long userId;
    private String accessToken;
    private String kakaoAccessToken;
    private ProfileImgDto profileImgDto;
    private NotificationDto notificationDto;
}
