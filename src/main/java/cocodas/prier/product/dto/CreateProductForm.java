package cocodas.prier.product.dto;

import lombok.Data;

@Data
public class CreateProductForm {
    private String productName;
    private int price;
    private String description;
    private int stock;
}
