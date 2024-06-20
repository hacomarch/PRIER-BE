package cocodas.prier.orders.orderproduct.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class OrderProductDto {
    private Long orderProductId;
    private Integer count;
    private Integer unitPrice;
    private Integer totalPrice;
    private Long productId;
    private Long orderId;
}
