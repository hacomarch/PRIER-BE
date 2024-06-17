package cocodas.prier.point.kakao.response;

import lombok.Getter;

@Getter
public class BaseResponse<T> {

    private int status;
    private String message;
    private T data;

    public BaseResponse(int status, String message) {
        this.status = status;
        this.message = message;
        this.data = null;
    }
}