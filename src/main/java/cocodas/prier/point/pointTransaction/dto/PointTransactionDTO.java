package cocodas.prier.point.pointTransaction.dto;

import cocodas.prier.point.pointTransaction.TransactionType;
import lombok.Builder;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@Builder
public class PointTransactionDTO {
    private Long transactionId;
    private Integer amount;
    private TransactionType transactionType;
    private LocalDateTime createdAt;
    private Integer balance;
    private Long userId;
    private String tid;
}

