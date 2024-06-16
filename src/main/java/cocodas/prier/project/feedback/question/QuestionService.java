package cocodas.prier.project.feedback.question;

import cocodas.prier.project.feedback.question.dto.QuestionDto;
import cocodas.prier.project.project.Project;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
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

        handleQuestions(project, questions, null,  types);
    }

    //질문은 삭제 추가만 가능하고 응답도 질문이 삭제되면 같이 삭제
    @Transactional
    public void updateQuestions(Project project, String[] questions, Long[] questionIds, String[] types) {
        if (questions.length != types.length) {
            throw new IllegalArgumentException("질문과 타입 배열의 길이가 일치하지 않습니다.");
        }

        // 기존 질문을 ID를 키로 하여 맵에 저장
        Map<Long, Question> existingQuestions = project.getFeedbackQuestions().stream()
                .collect(Collectors.toMap(Question::getQuestionId, question -> question));

        // 기존 컬렉션을 비우고, 질문을 새로 추가하거나 업데이트
        project.getFeedbackQuestions().clear();

        for (int i = 0; i < questions.length; i++) {
            Category category = types[i].equalsIgnoreCase("objective") ? Category.OBJECTIVE : Category.SUBJECTIVE;
            Question question = null;
            if (questionIds[i] != null) {
                // 기존 질문 업데이트
                question = existingQuestions.get(questionIds[i]);
                if (question != null) {
                    question.changeOrderIndex(i);
                    existingQuestions.remove(questionIds[i]); // 이제 삭제할 필요가 없음
                }
            }
            if (question == null) { // 기존 질문이 없거나 새로운 질문이라면
                question = Question.builder()
                        .content(questions[i])
                        .category(category)
                        .orderIndex(i)
                        .project(project)
                        .build();
            }
            project.getFeedbackQuestions().add(question);
        }

        // 더 이상 존재하지 않는 질문 삭제
        questionRepository.deleteAll(existingQuestions.values());
    }



    private void handleQuestions(Project project, String[] questions, Long[] questionId, String[] types) {
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

    public List<QuestionDto> getProjectQuestions(Project project) {
        return project.getFeedbackQuestions().stream()
                .map(question -> new QuestionDto(
                        question.getQuestionId(),
                        question.getContent(),
                        question.getCategory().name(),
                        question.getOrderIndex()))
                .collect(Collectors.toList());
    }

//    @Transactional
//    public String deleteQuestion(Long questionId, String token) {
//
//        Users user = getUsersByToken(token);
//
//        Question question = questionRepository.findById(questionId)
//                .orElseThrow(() -> new RuntimeException("존재하지 않는 질문"));
//
//        if (!question.getProject().getUsers().equals(user)) {
//            return "잘못된 사용자, 삭제 실패";
//        }
//
//        questionRepository.deleteById(questionId);
//
//        return "질문 삭제 완료";
//    }

}
