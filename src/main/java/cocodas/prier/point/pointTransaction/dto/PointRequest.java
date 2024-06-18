package cocodas.prier.point.pointTransaction.dto;

import lombok.Data;

@Data
public class PointRequest {
    private Long userId;
    private Integer amount;
}