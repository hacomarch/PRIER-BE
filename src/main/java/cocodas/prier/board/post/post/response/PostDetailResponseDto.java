package cocodas.prier.board.post.post.response;

import cocodas.prier.board.comment.response.PostCommentListResponseDto;
import cocodas.prier.user.response.ProfileImgDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class PostDetailResponseDto {
    private Long userId;
    private Long postId;
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
    private List<PostCommentListResponseDto> comments;
    private ProfileImgDto profileImgDto;
}
