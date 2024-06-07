package cocodas.prier.point.dto;

import cocodas.prier.point.PointTransaction;
import cocodas.prier.point.dto.PointTransactionDTO;

// DTO 변환을 위한 Mapper 클래스
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
