package cocodas.prier.point.kakao.response;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class KakaoCancelResponse {

    private String aid; // 요청 고유 번호
    private String tid; // 결제 고유 번호
    private String status; // 결제 상태
    private String payment_method_type; // 결제 수단
    private Amount amount; // 결제 금액 정보
    private ApprovedCancelAmount approved_cancel_amount; // 이번 요청으로 취소된 금액
    private String canceled_at; // 결제 취소 시각
}
