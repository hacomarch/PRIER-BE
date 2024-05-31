package cocodas.prier.orders.orders;

import cocodas.prier.orders.orderproduct.OrderProduct;
import cocodas.prier.user.Users;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Orders {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long orderId;
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private Users users;

    @OneToMany(mappedBy = "orders", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderProduct> orderProducts = new ArrayList<>();
}
