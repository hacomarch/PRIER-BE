package cocodas.prier.point.dto;

import cocodas.prier.point.TransactionType;
import lombok.Data;

@Data
public class PointRequest {
    private TransactionType transactionType;
}