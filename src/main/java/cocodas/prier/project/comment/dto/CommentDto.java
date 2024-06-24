package cocodas.prier.project.comment.dto;

import cocodas.prier.user.response.ProfileImgDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentDto {

    private Long commentId;
    private Long userId;
    private String userName;
    private String content;
    private Float score;
    private Boolean isMine = false;
    private ProfileImgDto profileImageUrl;
}
