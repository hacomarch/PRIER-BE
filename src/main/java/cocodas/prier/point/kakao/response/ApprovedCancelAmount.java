package cocodas.prier.point.kakao.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ApprovedCancelAmount {
    private int total; // 이번 요청으로 취소된 전체 금액
    private int tax_free; // 이번 요청으로 취소된 비과세 금액
}
