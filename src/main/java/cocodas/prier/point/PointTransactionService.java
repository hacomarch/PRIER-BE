package cocodas.prier.point;

import cocodas.prier.point.dto.PointRechargeRequest;
import cocodas.prier.point.dto.PointTransactionDTO;
import cocodas.prier.user.UserRepository;
import cocodas.prier.user.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PointTransactionService {

    @Autowired
    private PointTransactionRepository pointTransactionRepository;

    @Autowired
    private UserRepository userRepository;

    // 현재 포인트 조회
    public Integer getCurrentPoints(Long userId) {
        Users user = userRepository.findById(userId).orElseThrow();
        return user.getBalance(); // users 수정 후 재수정 예정
    }

    // 포인트 트랜잭션 내역 조회
    public List<PointTransactionDTO> getPointHistory(Long userId) {
        return pointTransactionRepository.findByUserId(userId).stream()
                .map(this::convertToDto)
                .collect(Collectors.toList());
    }

    // 포인트 충전 (POINT_CHARGE)
    public PointTransactionDTO rechargePoints(PointRechargeRequest request) {
        Users user = userRepository.findById(request.getUserId()).orElseThrow();
        PointTransaction transaction = PointTransaction.builder()
                .amount(request.getAmount())
                .transactionType(TransactionType.POINT_CHARGE)
                .createdAt(LocalDateTime.now())
                // users 수정 후 재수정 예정
                .balance(user.getBalance() + request.getAmount())
                .users(user)
                .build();

        user.setBalance(user.getBalance() + request.getAmount());
        userRepository.save(user);
        pointTransactionRepository.save(transaction);

        return convertToDto(transaction);
    }

    private PointTransactionDTO convertToDto(PointTransaction transaction) {
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
