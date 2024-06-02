package cocodas.prier.point;

import cocodas.prier.user.Users;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class PointTransaction {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;
    private Integer amount;
    private TransactionType transactionType;
    private LocalDateTime createdAt;
    private Integer balance;

    @OneToOne
    @JoinColumn(name = "user_id")
    private Users users;

}
