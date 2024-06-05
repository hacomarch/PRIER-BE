package cocodas.prier.point;

import cocodas.prier.point.dto.PointRechargeRequest;
import cocodas.prier.point.dto.PointTransactionDTO;
import cocodas.prier.project.project.Project;
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

        user.updateBalance(request.getAmount());
        userRepository.save(user);
        pointTransactionRepository.save(transaction);

        return convertToDto(transaction);
    }

    // 피드백 기간 연장 (FEEDBACK_EXTENSION)
    public PointTransactionDTO extendFeedbackPeriod(Long userId, Long projectId, int weeks) {
        Users user = userRepository.findById(userId).orElseThrow();
        Project project = projectRepository.findById(projectId).orElseThrow(); // project 수정 후 재수정 예정

        int cost = weeks * 500 // 1주당 500 포인트로 설정
        if (user.getBalance() < cost) { // Users 수정후 재수정 예정
            throw new IllegalArgumentException("포인트가 부족합니다.");
        }

        project.extendFeedbackPeriod(weeks);
        user.updateBalance(-cost);

        PointTransaction transaction = PointTransaction.builder()
                .amount(-cost)
                .transactionType(TransactionType.FEEDBACK_EXTENSION)
                .createdAt(LocalDateTime.now())
                .balance(user.getBalance()) // Users 수정후 재수정 예정
                .users(user)
                .build();

        userRepository.save(user);
        projectRepository.save(project); // project 수정 후 재수정 예정
        pointTransactionRepository.save(transaction);

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
