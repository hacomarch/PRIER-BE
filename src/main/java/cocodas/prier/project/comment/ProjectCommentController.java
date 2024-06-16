package cocodas.prier.project.comment;

import cocodas.prier.project.comment.dto.CommentDto;
import cocodas.prier.project.comment.dto.CommentForm;
import cocodas.prier.project.comment.dto.MyPageCommentDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects")
public class ProjectCommentController {

    private final ProjectCommentService projectCommentService;

    private static String getToken(String auth) {
        if (auth == null || !auth.startsWith("Bearer ")) {
            throw new RuntimeException("JWT Token is missing");
        }

        return auth.substring(7);
    }

    @PostMapping("/{projectId}/comment")
    public ResponseEntity<CommentDto> createComment(@PathVariable Long projectId,
                                        @RequestBody CommentForm form,
                                        @RequestHeader("Authorization") String auth) {

        String token = getToken(auth);
        CommentDto result = projectCommentService.createProjectComment(projectId, form, token);

        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{projectId}/comment/{commentId}")
    public ResponseEntity<String> deleteComment(@PathVariable Long projectId,
                                                    @PathVariable Long commentId,
                                                    @RequestHeader("Authorization") String auth) {
        String token = getToken(auth);
        String result = projectCommentService.deleteProjectComment(projectId, commentId, token);

        return ResponseEntity.ok(result);
    }

    @PutMapping("/{projectId}/comment/{commentId}")
    public ResponseEntity<CommentDto> updateComment(@PathVariable Long projectId,
                                                    @PathVariable Long commentId,
                                                    @RequestBody CommentForm form,
                                                    @RequestHeader("Authorization") String auth) {
        String token = getToken(auth);
        CommentDto result = projectCommentService.updateProjectComment(projectId, commentId, form, token);

        return ResponseEntity.ok(result);
    }

    @GetMapping("/{projectId}/comment")
    public List<CommentDto> getProjectComments(@PathVariable Long projectId,
                                               @RequestHeader("Authorization") String auth) {

        String token = getToken(auth);
        return projectCommentService.getProjectComments(projectId, token);
    }

    @GetMapping("/comment/my-comments")
    public List<MyPageCommentDto> getMyProjectComments(@RequestHeader("Authorization") String auth) {
        String token = getToken(auth);
        return projectCommentService.getMyProjectComments(token);
    }
}
