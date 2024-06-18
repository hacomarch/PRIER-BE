package cocodas.prier.point.kakao.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class CancelAvailableAmount {
    private int total;
    private int tax_free;
}
