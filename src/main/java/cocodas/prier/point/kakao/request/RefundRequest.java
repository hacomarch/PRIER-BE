package cocodas.prier.point.kakao.request;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class RefundRequest {
    private String tid;
    private int cancelAmount;
    private int cancelTaxFreeAmount;
}
