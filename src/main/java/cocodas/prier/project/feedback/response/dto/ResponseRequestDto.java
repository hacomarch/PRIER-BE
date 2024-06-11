package cocodas.prier.project.feedback.response.dto;

import lombok.Data;
import org.antlr.v4.runtime.misc.NotNull;

@Data
public class ResponseRequestDto {

    private Long questionId;
    private String content;
    private Long userId;
}
