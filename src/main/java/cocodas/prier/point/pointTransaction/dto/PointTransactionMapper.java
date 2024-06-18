package cocodas.prier.point.pointTransaction.dto;

import cocodas.prier.point.pointTransaction.PointTransaction;

public class PointTransactionMapper {

    public static PointTransactionDTO toDto(PointTransaction transaction) {
        return PointTransactionDTO.builder()
                .transactionId(transaction.getTransactionId())
                .amount(transaction.getAmount())
                .transactionType(transaction.getTransactionType())
                .createdAt(transaction.getCreatedAt())
                .balance(transaction.getBalance())
                .userId(transaction.getUsers().getUserId())
                .tid(transaction.getTid())
                .build();
    }
}
