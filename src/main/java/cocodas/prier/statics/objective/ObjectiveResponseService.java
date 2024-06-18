package cocodas.prier.statics.objective;

import cocodas.prier.project.feedback.question.Category;
import cocodas.prier.project.feedback.question.Question;
import cocodas.prier.project.project.ProjectRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ObjectiveResponseService {
    private final ProjectRepository projectRepository;

    //1. 각 질문마다 응답을 찾아서 평균을 구하면서 모든 질문의 응답에 대한 평균 구하기
    //2. 백분율 값 구하기 -> (모든 응답 평균 / 50) * 100
    public Double calculateFeedbackPercentage(Long projectId) {
        long startTime = System.currentTimeMillis();

        double average = getObjectiveQuestionsByProjectId(projectId)
                .stream()
                .flatMap(question -> question.getResponses().stream())
                .mapToDouble(response -> Double.parseDouble(response.getContent()))
                .average()
                .orElse(0.0);

        System.out.println("Query Entity Graph execution time: " + (System.currentTimeMillis() - startTime) + " ms");

        return (average / 50.0) * 100;
    }


    //projectId로 project 찾아오고 객관식 질문 리스트를 가져오기
    private List<Question> getObjectiveQuestionsByProjectId(Long projectId) {
        return projectRepository.findWithQuestionsAndResponsesByProjectId(projectId)
                .map(project -> project
                        .getFeedbackQuestions()
                        .stream()
                        .filter(question -> question
                                .getCategory()
                                .equals(Category.OBJECTIVE))
                        .toList())
                .orElse(Collections.emptyList());
    }
}
