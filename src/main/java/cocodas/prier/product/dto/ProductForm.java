package cocodas.prier.product.dto;

import lombok.Data;

@Data
public class ProductForm {
    private String productName;
    private int price;
    private String description;
    private int stock;
}
