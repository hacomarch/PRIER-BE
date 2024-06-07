package cocodas.prier.project.feedback.question.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class QuestionDto {
    private Long questionId;
    private String content;
    private String category;
    private int orderIndex;
}
