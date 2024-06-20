package cocodas.prier.point.pointTransaction;

import cocodas.prier.point.pointTransaction.PointTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface PointTransactionRepository extends JpaRepository<PointTransaction, Long> {
    List<PointTransaction> findByUsers_UserId(Long userId);
    Optional<PointTransaction> findByTid(String tid);
    boolean existsByUsers_UserIdAndTransactionIdGreaterThanAndTransactionTypeIn(Long userId, Long transactionId, List<TransactionType> transactionTypes);
}
