package cocodas.prier.board.post.post.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@AllArgsConstructor
public class PostResponseDto {
    private Long postId;
    private Long writerId;
    private String writerProfileUrl;
    private String title;
    private String content;
    private String nickname;
    private String category;
    private Boolean isLikedByMe;
    private List<PostMediaDto> media;
    private int views;
    private int likes;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
