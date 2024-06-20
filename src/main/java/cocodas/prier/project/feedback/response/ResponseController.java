package cocodas.prier.project.feedback.response;

import cocodas.prier.project.feedback.response.dto.UserResponseProjectDto;
import cocodas.prier.project.feedback.response.dto.ResponseDto;
import cocodas.prier.project.feedback.response.dto.ResponseRequestDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

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

    // 응답 등록하기
    @PostMapping("/{projectId}/responses")
    public ResponseEntity<List<ResponseDto>> createResponses(@PathVariable(name = "projectId") Long projectId,
                                                             @RequestBody List<ResponseRequestDto> responsesDto,
                                                             Authentication authentication) {
        if (responsesDto.isEmpty()) {
            throw new IllegalArgumentException("Responses cannot be empty");
        }
        Long userId = Long.valueOf(authentication.getName());
        List<ResponseDto> createdResponses = responseService.createResponses(userId, responsesDto);
        return ResponseEntity.ok(createdResponses);
    }

    // 프로젝트별 응답 조회
    @GetMapping("/{projectId}/responses")
    public ResponseEntity<List<ResponseDto>> getResponsesByProject(@PathVariable(name = "projectId") Long projectId,
                                                                   Authentication authentication) {
        Long userId = Long.valueOf(authentication.getName());
        List<ResponseDto> responses = responseService.getResponsesByProject(projectId);
        return ResponseEntity.ok(responses);
    }

    // 자신의 피드백 삭제
    @DeleteMapping("/{projectId}/responses")
    public ResponseEntity<String> deleteResponsesByUserAndProject(@PathVariable(name = "projectId") Long projectId,
                                                                  Authentication authentication) {
        Long userId = Long.valueOf(authentication.getName());
        responseService.deleteResponses(projectId, userId);
        return ResponseEntity.ok("Responses for project ID " + projectId + " by user ID " + userId + " have been deleted.");
    }

    // 자신이 응답을 남긴 프로젝트 목록 조회
    @GetMapping("/my-feedbacks")
    public ResponseEntity<List<UserResponseProjectDto>> getProjectsByUser(Authentication authentication) {
        Long userId = Long.valueOf(authentication.getName());
        List<Long> projectIds = responseService.getProjectsByUser(userId);
        List<UserResponseProjectDto> projectDtos = projectIds.stream()
                .map(projectId -> UserResponseProjectDto.builder().projectId(projectId).build())
                .collect(Collectors.toList());
        return ResponseEntity.ok(projectDtos);
    }

}
