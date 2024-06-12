package cocodas.prier.board.post.like;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class LikeController {

    private final LikeService likeService;

    // 게시글에 좋아요 누르기
    @PostMapping("/like/{postId}")
    @ResponseStatus(HttpStatus.CREATED)
    public void pushLike(@RequestHeader("Authorization") String authorizationHeader,
                         @PathVariable(name = "postId") Long postId) {
        String token = authorizationHeader.replace("Bearer ", "");
        likeService.pushLike(token, postId);
    }
}
