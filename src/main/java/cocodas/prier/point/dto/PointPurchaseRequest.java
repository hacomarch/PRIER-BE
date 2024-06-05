package cocodas.prier.point.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PointPurchaseRequest {
    private Long productId;
    private Long userId;
}
