package cocodas.prier.project.feedback.response;

import cocodas.prier.project.feedback.response.dto.ResponseDto;
import cocodas.prier.project.feedback.response.dto.ResponseRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects")
public class ResponseController {

    private final ResponseService responseService;

    private static String getToken(String auth) {
        if (auth == null || !auth.startsWith("Bearer ")) {
            throw new RuntimeException("JWT Token is missing");
        }

        return auth.substring(7);
    }

    // 여러 질문에 대한 응답 생성
    @PostMapping("/{projectId}/feedbacks/responses")
    public ResponseEntity<List<ResponseDto>> createResponses(@PathVariable Long projectId,
                                                             @RequestBody List<ResponseRequestDto> responsesDto) {
        if (responsesDto.isEmpty()) {
            throw new IllegalArgumentException("Responses cannot be empty");
        }
        Long userId = responsesDto.get(0).getUserId();
        if (userId == null) {
            throw new IllegalArgumentException("userId is required");
        }
        List<ResponseDto> createdResponses = responseService.createResponses(userId, responsesDto);
        return ResponseEntity.ok(createdResponses);
    }

    // 질문별 응답 조회
    @GetMapping("/{projectId}/feedbacks/{questionId}/responses")
    public ResponseEntity<List<ResponseDto>> getResponsesByQuestion(@PathVariable Long projectId,
                                                                    @PathVariable Long questionId) {
        List<ResponseDto> responses = responseService.getResponsesByQuestion(questionId);
        return ResponseEntity.ok(responses);
    }

    // 프로젝트별 응답 조회
    @GetMapping("/{projectId}/responses")
    public ResponseEntity<List<ResponseDto>> getResponsesByProject(@PathVariable Long projectId) {
        List<ResponseDto> responses = responseService.getResponsesByProject(projectId);
        return ResponseEntity.ok(responses);
    }

    // 자신의 피드백 삭제
    @DeleteMapping("/{projectId}/responses")
    public ResponseEntity<String> deleteResponsesByUserAndProject(@PathVariable Long projectId,
                                                                  @RequestParam Long userId) {
        responseService.deleteResponsesByUserAndProject(projectId, userId);
        return ResponseEntity.ok("프로젝트 ID " + projectId + "에 대한 유저 ID " + userId + " 의 응답 삭제 완료");
    }
}