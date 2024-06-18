package cocodas.prier.point.pointTransaction;

import cocodas.prier.point.pointTransaction.PointTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointTransactionRepository extends JpaRepository<PointTransaction, Long> {
    List<PointTransaction> findByUsers_UserId(Long userId);
}
