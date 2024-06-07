package cocodas.prier.point.dto;

import cocodas.prier.point.PointTransaction;
import cocodas.prier.point.dto.PointTransactionDTO;

public class PointTransactionMapper {

    public static PointTransactionDTO toDto(PointTransaction transaction) {
        return PointTransactionDTO.builder()
                .transactionId(transaction.getTransactionId())
                .amount(transaction.getAmount())
                .transactionType(transaction.getTransactionType())
                .createdAt(transaction.getCreatedAt())
                .balance(transaction.getBalance())
                .userId(transaction.getUsers().getUserId())
                .build();
    }
}
