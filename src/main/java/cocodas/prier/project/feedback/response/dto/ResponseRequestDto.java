package cocodas.prier.project.feedback.response.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResponseRequestDto {
    private Long questionId;
    private String content;
}
