package cocodas.prier.board.post.like;

import cocodas.prier.board.post.like.request.LikeRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LikeController {

    private final LikeService likeService;

    // 게시글에 좋아요 누르기
    @PostMapping("/like")
    public void pushLike(@RequestHeader("Authorization") String authorizationHeader,
                         @RequestBody LikeRequestDto likeRequestDto) {
        String token = authorizationHeader.replace("Bearer ", "");
        likeService.pushLike(token, likeRequestDto);
    }
}
