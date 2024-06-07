package cocodas.prier.point;

import cocodas.prier.point.dto.PointRechargeRequest;
import cocodas.prier.point.dto.PointTransactionDTO;
import cocodas.prier.point.dto.PointTransactionMapper;
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

    @Autowired
    public PointTransactionService(PointTransactionRepository pointTransactionRepository, UserRepository userRepository) {
        this.pointTransactionRepository = pointTransactionRepository;
        this.userRepository = userRepository;
    }

    // 현재 포인트 조회
    public Integer getCurrentPoints(Long userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));
        return user.getBalance();
    }

    // 포인트 트랜잭션 내역 조회
    public List<PointTransactionDTO> getPointHistory(Long userId) {
        return pointTransactionRepository.findByUsers_UserId(userId).stream()
                .map(PointTransactionMapper::toDto)
                .collect(Collectors.toList());
    }

    // 포인트 충전 (POINT_CHARGE)
    public PointTransactionDTO rechargePoints(PointRechargeRequest request, Long userId) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found with id: " + userId));

        return processTransaction(user, request.getAmount(), TransactionType.POINT_CHARGE);
    }

    // 포인트 차감 (PRODUCT_PURCHASE, FEEDBACK_EXTENSION)
    @Transactional
    public PointTransactionDTO deductPoints(Users user, Integer amount, TransactionType transactionType) {
        if (user.getBalance() < amount) {
            throw new IllegalArgumentException("Insufficient points.");
        }

        return processTransaction(user, -amount, transactionType);
    }

    private PointTransactionDTO processTransaction(Users user, Integer amount, TransactionType transactionType) {
        user.updateBalance(amount);

        PointTransaction transaction = PointTransaction.builder()
                .amount(amount)
                .transactionType(transactionType)
                .createdAt(LocalDateTime.now())
                .balance(user.getBalance())
                .users(user)
                .build();

        userRepository.save(user);
        pointTransactionRepository.save(transaction);

        return PointTransactionMapper.toDto(transaction);
    }
}
