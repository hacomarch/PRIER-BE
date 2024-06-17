package cocodas.prier.point;

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
                .orElseThrow(() -> new IllegalArgumentException("해당 유저 조회 불가: " + userId));
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
    public PointTransactionDTO increasePoints(Users user, Integer amount, TransactionType transactionType) {
        return processTransaction(user, amount, transactionType);
    }

    // 포인트 감소
    @Transactional
    public PointTransactionDTO decreasePoints(Users user, Integer amount, TransactionType transactionType) {
        if (user.getBalance() < amount) {
            throw new IllegalArgumentException("포인트가 부족합니다.");
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
