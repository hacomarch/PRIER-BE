package cocodas.prier.statics.objective;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ObjectiveResponseController {
    private final ObjectiveResponseService objectiveResponseService;

    //TODO : 프로젝트 댓글 별점 기능 만들어지면 그것도 추가해서 응답 변경해야 함.
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/api/{projectId}/statics/objective")
    public String getFeedbackPercentage(@PathVariable Long projectId) {
        Double v = objectiveResponseService.calculateFeedbackPercentage(projectId);
        return v + " %";
    }
}
