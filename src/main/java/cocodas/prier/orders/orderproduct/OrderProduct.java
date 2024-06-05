package cocodas.prier.orders.orderproduct;

import cocodas.prier.orders.orders.Orders;
import cocodas.prier.product.Product;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class OrderProduct {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long orderProductId;

    @Column(nullable = false)
    private Integer count;

    @Column(nullable = false)
    private Integer unitPrice;

    @Column(nullable = false)
    private Integer totalPrice;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Orders orders;

    @Builder
    public OrderProduct(Product product, Orders orders, Integer count, Integer unitPrice) {
        this.product = product;
        this.orders = orders;
        this.count = count;
        this.unitPrice = unitPrice;
        this.totalPrice = calculateTotalPrice();

    }

    private Integer calculateTotalPrice() {
        return this.unitPrice * this.count;
    }

    public void changeCount(Integer count) {
        this.count = count;
        this.totalPrice = calculateTotalPrice();
    }

    public void changeUnitPrice(Integer unitPrice) {
        this.unitPrice = unitPrice;
        this.totalPrice = calculateTotalPrice();
    }
}
