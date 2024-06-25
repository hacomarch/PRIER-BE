package cocodas.prier.statics.chatgpt.response;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SummaryResponse {
    private Long question_id;
    private String questionContent;
    private Integer feedbackCount;
    private String summary;
}
