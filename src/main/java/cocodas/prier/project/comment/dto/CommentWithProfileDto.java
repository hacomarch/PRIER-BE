package cocodas.prier.project.comment.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentWithProfileDto {
    private Long commentId;
    private Long userId;
    private String userName;
    private String content;
    private Float score;
    private Boolean isMine = false;
    private String profileUrl;
}
