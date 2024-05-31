package cocodas.prier.product;

import cocodas.prier.orders.orderproduct.OrderProduct;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Product {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long productId;
    private String productName;
    private Integer price;
    private String description;
    private Integer stock;

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<OrderProduct> orderProducts = new ArrayList<>();

}
