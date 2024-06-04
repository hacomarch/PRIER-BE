package cocodas.prier.point;

import cocodas.prier.user.Users;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PointTransaction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long transactionId;

    private Integer amount;

    @Enumerated(EnumType.STRING)
    private TransactionType transactionType;

    private LocalDateTime createdAt;

    private Integer balance;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private Users users;
}
