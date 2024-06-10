package cocodas.prier.project.feedback.response.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseDto {
    private Long responseId;
    private String content;
    private String createdAt;
    private Long questionId;
    private Long userId;
}
