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
    private Boolean isMine = false;


    public CommentDto(Long commentId, String userName, String content, Float score) {
        this.commentId = commentId;
        this.userName = userName;
        this.content = content;
        this.score = score;
    }
}
