package cocodas.prier.point.pointTransaction;

import cocodas.prier.point.pointTransaction.dto.PointTransactionDTO;
import cocodas.prier.point.pointTransaction.dto.PointTransactionMapper;
import cocodas.prier.user.UserRepository;
import cocodas.prier.user.Users;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PointTransactionService {

    private static final Logger logger = LoggerFactory.getLogger(PointTransactionService.class);

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
                .orElseThrow(() -> new IllegalArgumentException("User not found: " + userId));
        return user.getBalance();
    }

    // 포인트 트랜잭션 내역 조회
    public List<PointTransactionDTO> getPointHistory(Long userId) {
        return pointTransactionRepository.findByUsers_UserId(userId).stream()
                .map(PointTransactionMapper::toDto)
                .collect(Collectors.toList());
    }

    // 포인트 증가
    @Transactional
    public PointTransactionDTO increasePoints(Users user, Integer amount, TransactionType transactionType, String tid) {
        logger.info("Increasing points for user {} by {} points", user.getUserId(), amount);
        PointTransactionDTO result = processTransaction(user, amount, transactionType, tid);
        logger.info("Points increased for user {}: new balance is {}", user.getUserId(), user.getBalance());
        return result;
    }

    // 포인트 감소
    @Transactional
    public PointTransactionDTO decreasePoints(Users user, Integer amount, TransactionType transactionType) {
        if (user.getBalance() < amount) {
            logger.warn("Attempt to deduct {} points from user {} failed due to insufficient balance", amount, user.getUserId());
            throw new IllegalArgumentException("Insufficient points.");
        }
        logger.info("Decreasing points for user {} by {} points", user.getUserId(), amount);
        PointTransactionDTO result = processTransaction(user, -amount, transactionType, null);
        logger.info("Points decreased for user {}: new balance is {}", user.getUserId(), user.getBalance());
        return result;
    }

    private PointTransactionDTO processTransaction(Users user, Integer amount, TransactionType transactionType, String tid) {
        user.updateBalance(amount);

        PointTransaction transaction = PointTransaction.builder()
                .amount(amount)
                .transactionType(transactionType)
                .createdAt(LocalDateTime.now())
                .balance(user.getBalance())
                .users(user)
                .tid(tid)
                .build();

        userRepository.save(user);
        pointTransactionRepository.save(transaction);

        logger.info("Processed transaction for user {}: amount={}, transactionType={}, new balance={}",
                user.getUserId(), amount, transactionType, user.getBalance());

        return PointTransactionMapper.toDto(transaction);
    }
}
