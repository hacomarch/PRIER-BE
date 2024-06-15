package cocodas.prier.project.feedback.question;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class QuestionController {

    private final QuestionService questionService;

    private static String getToken(String auth) {
        if (auth == null || !auth.startsWith("Bearer ")) {
            throw new RuntimeException("JWT Token is missing");
        }

        return auth.substring(7);
    }

    @DeleteMapping("/api/questions/{questionId}")
    public ResponseEntity<String> deleteQuestion(@PathVariable Long questionId,
                                                 @RequestHeader("Authentication") String auth) {

        String token = getToken(auth);
        String result = questionService.deleteQuestion(questionId, token);

        return ResponseEntity.ok(result);
    }
}
