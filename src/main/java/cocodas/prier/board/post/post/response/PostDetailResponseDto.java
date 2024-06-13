package cocodas.prier.board.post.post.response;

import cocodas.prier.board.comment.response.PostCommentListResponseDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class PostDetailResponseDto {
    private String title;
    private String content;
    private String nickname;
    private String category;
    private List<PostMediaDto> media;
    private int views;
    private int likes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<PostCommentListResponseDto> comments;
}
