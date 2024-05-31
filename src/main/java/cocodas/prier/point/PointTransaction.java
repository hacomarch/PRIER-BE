package cocodas.prier.point;

import cocodas.prier.user.Users;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class PointTransaction {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long transactionId;
    private Integer amount;
    private TransactionType transactionType;
    private LocalDateTime createdAt;
    private Integer balance;

    @OneToOne(mappedBy = "pointTransaction")
    private Users users;

}
