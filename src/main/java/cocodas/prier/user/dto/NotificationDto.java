package cocodas.prier.user.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NotificationDto {
    private Long responseAmount;
    private Long commentAmount;
}
