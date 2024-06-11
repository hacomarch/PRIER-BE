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
@RequestMapping("/api/boards/{boardId}/comment")
public class PostCommentController {
    private final PostCommentService postCommentService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public void createComment(@RequestHeader("Authorization") String auth,
                              @RequestBody PostCommentRequestDto dto,
                              @PathVariable Long boardId) {
        String token = getToken(auth);
        postCommentService.savePostComment(token, boardId, dto);
    }

    @PutMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public void updateComment(@RequestHeader("Authorization") String auth,
                              @RequestBody PostCommentRequestDto dto,
                              @PathVariable Long commentId) {
        String token = getToken(auth);
        postCommentService.updatePostComment(token, commentId, dto);
    }

    @DeleteMapping("/{commentId}")
    @ResponseStatus(HttpStatus.OK)
    public void deleteComment(@RequestHeader("Authorization") String auth,
                              @PathVariable Long commentId) {
        String token = getToken(auth);
        postCommentService.deletePostComment(token, commentId);
    }

    @GetMapping
    public ResponseEntity<List<PostCommentListResponseDto>> findByPostId(@PathVariable Long boardId) {
        return ResponseEntity.ok().body(postCommentService.findPostCommentByPostId(boardId));
    }

    private String getToken(String auth) {
        return auth.replace("Bearer ", "");
    }
}
