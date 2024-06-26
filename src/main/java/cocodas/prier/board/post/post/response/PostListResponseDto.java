package cocodas.prier.board.post.post.response;

import cocodas.prier.user.response.ProfileImgDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class PostListResponseDto {
    List<PostResponseDto> postListDto;
    private ProfileImgDto myProfileImgDto;
}
