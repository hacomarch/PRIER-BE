package cocodas.prier.point;

import cocodas.prier.point.dto.PointRechargeRequest;
import cocodas.prier.point.dto.PointTransactionDTO;
import cocodas.prier.project.project.Project;
import cocodas.prier.project.project.ProjectRepository;
import cocodas.prier.user.UserRepository;
import cocodas.prier.user.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PointTransactionService {

    private final PointTransactionRepository pointTransactionRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    @Autowired
    public PointTransactionService(PointTransactionRepository pointTransactionRepository, UserRepository userRepository, ProjectRepository projectRepository) {
        this.pointTransactionRepository = pointTransactionRepository;
        this.userRepository = userRepository;
        this.projectRepository = projectRepository;
    }

    // 현재 포인트 조회
    public Integer getCurrentPoints(Long userId) {
        Users user = userRepository.findById(userId).orElseThrow();
        return user.getBalance();
    }

    // 포인트 트랜잭션 내역 조회
    public List<PointTransactionDTO> getPointHistory(Long userId) {
        return pointTransactionRepository.findByUsers_UserId(userId).stream()
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
                .balance(user.getBalance() + request.getAmount())
                .users(user)
                .build();

        user.updateBalance(user.getBalance() + request.getAmount());
        user.updateBalance(request.getAmount());
        userRepository.save(user);
        pointTransactionRepository.save(transaction);

        return convertToDto(transaction);
    }

    // 피드백 기간 연장 (FEEDBACK_EXTENSION)
    public PointTransactionDTO extendFeedbackPeriod(Long userId, Long projectId, int weeks) {
        Users user = userRepository.findById(userId).orElseThrow();
        Project project = projectRepository.findById(projectId).orElseThrow();

        int cost = weeks * 250; // 한 주당 250 포인트 소모로 설정
        if (user.getBalance() < cost) {
            throw new IllegalArgumentException("포인트가 부족합니다.");
        }

        //project.extendFeedbackPeriod(weeks);
        user.updateBalance(-cost);

        PointTransaction transaction = PointTransaction.builder()
                .amount(-cost)
                .transactionType(TransactionType.FEEDBACK_EXTENSION)
                .createdAt(LocalDateTime.now())
                .balance(user.getBalance())
                .users(user)
                .build();

        userRepository.save(user);
        projectRepository.save(project);
        pointTransactionRepository.save(transaction);

        return convertToDto(transaction);
    }

    // 포인트 차감 (PRODUCT_PURCHASE)
    @Transactional
    public void deductPointsForPurchase(Users user, Integer amount) {
        if (user.getBalance() < amount) {
            throw new IllegalArgumentException("포인트가 부족합니다.");
        }
        user.updateBalance(-amount);

        PointTransaction transaction = PointTransaction.builder()
                .amount(-amount)
                .transactionType(TransactionType.PRODUCT_PURCHASE)
                .createdAt(LocalDateTime.now())
                .balance(user.getBalance())
                .users(user)
                .build();

        userRepository.save(user);
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
