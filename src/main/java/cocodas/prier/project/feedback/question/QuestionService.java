package cocodas.prier.project.feedback.question;

import cocodas.prier.project.project.Project;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuestionService {

    private final QuestionRepository questionRepository;

    @Transactional
    public void createQuestion(Project project, String[] questions, String[] types) {

        if (questions.length != types.length) {
            throw new IllegalArgumentException("질문과 타입 배열의 길이가 일치하지 않습니다.");
        }

        for (int i = 0; i < questions.length; i++) {
            Category category = Category.SUBJECTIVE; // 기본값 설정
            if (types[i].equalsIgnoreCase("objective")) {
                category = Category.OBJECTIVE;
            }

            Question question = Question.builder()
                    .content(questions[i])
                    .category(category)
                    .orderIndex(i)
                    .project(project) // 프로젝트 연결
                    .build();

            questionRepository.save(question); // 질문 저장
            project.getFeedbackQuestions().add(question);
        }
    }

    //todo : 질문 수정기능 구현해야함
    @Transactional
    public void updateQuestions(Project project, String[] questions, String[] types) {
        List<Question> existingQuestions = project.getFeedbackQuestions();

        if (questions.length != types.length) {
            throw new IllegalArgumentException("질문과 타입 배열의 길이가 일치하지 않습니다.");
        }


    }

}
