package cocodas.prier.board.comment.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@AllArgsConstructor
public class PostCommentListResponseDto {
    private Long userId;
    private Long commentId;
    private String content;
    private String nickname;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
