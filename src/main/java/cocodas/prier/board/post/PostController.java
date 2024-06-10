package cocodas.prier.board.post;

import cocodas.prier.board.post.post.PostRequestDto;
import cocodas.prier.board.post.post.PostService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    // 게시글 작성하기
    @PostMapping("/boards")
    @ResponseStatus(HttpStatus.CREATED)
    public void addPost(@RequestHeader("Authorization") String authorizationHeader,
                        @RequestBody PostRequestDto postRequestDto) {
        String token = authorizationHeader.replace("Bearer ", "");
        postService.addPost(token, postRequestDto);
    }

    // 게시글 수정하기
    @PutMapping("/boards/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public void updatePost(@RequestHeader("Authorization") String authorizationHeader,
                           @RequestBody PostRequestDto postRequestDto,
                           @PathVariable(name = "postId") Long postId) {
        String token = authorizationHeader.replace("Bearer ", "");
        postService.updatePost(token, postRequestDto, postId);
    }

    @DeleteMapping("/boards/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public void deletePost(@RequestHeader("Authorization") String authorizationHeader,
                           @PathVariable(name = "postId") Long postId) {
        String token = authorizationHeader.replace("Bearer ", "");
        postService.deletePost(token, postId);
    }
}
