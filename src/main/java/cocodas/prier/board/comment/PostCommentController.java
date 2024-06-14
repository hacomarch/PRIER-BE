package cocodas.prier.board.comment;

import cocodas.prier.board.comment.request.PostCommentRequestDto;
import cocodas.prier.board.comment.response.PostCommentListResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/posts/{postId}/comment")
public class PostCommentController {
    private final PostCommentService postCommentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createComment(@RequestHeader("Authorization") String auth,
                              @RequestBody PostCommentRequestDto dto,
                              @PathVariable(name = "postId") Long postId) {
        String token = getToken(auth);
        postCommentService.savePostComment(token, postId, dto);
    }

    @PutMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public void updateComment(@RequestHeader("Authorization") String auth,
                              @RequestBody PostCommentRequestDto dto,
                              @PathVariable(name = "commentId") Long commentId) {
        String token = getToken(auth);
        postCommentService.updatePostComment(token, commentId, dto);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteComment(@RequestHeader("Authorization") String auth,
                              @PathVariable(name = "commentId") Long commentId) {
        String token = getToken(auth);
        postCommentService.deletePostComment(token, commentId);
    }

    @GetMapping
    public ResponseEntity<List<PostCommentListResponseDto>> findByPostId(@PathVariable(name = "postId") Long postId) {
        return ResponseEntity.ok().body(postCommentService.findPostCommentByPostId(postId));
    }

    private String getToken(String auth) {
        return auth.replace("Bearer ", "");
    }
}
