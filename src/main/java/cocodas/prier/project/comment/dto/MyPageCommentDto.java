package cocodas.prier.project.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MyPageCommentDto {
    private Long projectId;
    private Long commentId;
    private String projectName;
    private String teamName;
    private String content;
    private Float score;
}
