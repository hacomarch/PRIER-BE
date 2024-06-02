package cocodas.prier.orders.orderproduct;

import cocodas.prier.orders.orders.Orders;
import cocodas.prier.product.Product;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class OrderProduct {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderProductId;
    private Integer count;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Orders orders;
}
