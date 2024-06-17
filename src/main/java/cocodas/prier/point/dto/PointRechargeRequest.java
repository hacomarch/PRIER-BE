package cocodas.prier.point.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class PointRechargeRequest extends PointRequest {
    private Integer amount;
}
