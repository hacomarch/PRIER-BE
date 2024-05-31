package cocodas.prier.orders.orderproduct;

import cocodas.prier.orders.orders.Orders;
import cocodas.prier.product.Product;
import jakarta.persistence.*;

@Entity
public class OrderProduct {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long orderProductId;
    private Integer count;

    @ManyToOne(fetch = FetchType.LAZY)
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    private Orders orders;
}
