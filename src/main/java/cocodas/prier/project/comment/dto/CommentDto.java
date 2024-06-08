package cocodas.prier.project.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentDto {

    private Long commentId;
    private String userName;
    private String content;
    private Float score;

}
