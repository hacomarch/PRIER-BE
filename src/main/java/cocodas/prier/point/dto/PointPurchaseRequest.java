package cocodas.prier.point.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;


@EqualsAndHashCode(callSuper = true)
@Data
public class PointPurchaseRequest extends PointRequest {
    private Long productId;
}
