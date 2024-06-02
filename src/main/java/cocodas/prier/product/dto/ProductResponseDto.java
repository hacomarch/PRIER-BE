package cocodas.prier.product.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProductResponseDto {
    private Long productId;
    private String productName;
    private int price;
    private String description;
    private int stock;
    private String imageUrl;
}
