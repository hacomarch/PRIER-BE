package cocodas.prier.point.kakao.response;

import lombok.Getter;

@Getter
public class PayReadyResDto {
    private String tid;
    private String next_redirect_pc_url;
    private String created_at;
}