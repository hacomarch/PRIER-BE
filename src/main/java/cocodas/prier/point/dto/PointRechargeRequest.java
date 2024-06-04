package cocodas.prier.point.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PointRechargeRequest {
    private Integer amount;
    private Long userId;
}
