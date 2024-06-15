package cocodas.prier.project.feedback.question;

import cocodas.prier.project.feedback.question.dto.QuestionDto;
import cocodas.prier.project.project.Project;
import cocodas.prier.user.UserRepository;
import cocodas.prier.user.Users;
import cocodas.prier.user.kakao.jwt.JwtTokenProvider;
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
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public void createQuestion(Project project, String[] questions, String[] types) {

        if (questions.length != types.length) {
            throw new IllegalArgumentException("질문과 타입 배열의 길이가 일치하지 않습니다.");
        }

        handleQuestions(project, questions, types);
    }

    //todo : 질문 수정은 못함. 질문은 삭제 추가만 가능하고 응답도 질문이 삭제되면 같이 삭제
//    @Transactional
//    public void updateQuestions(Project project, String[] questions, String[] types) {
//        project.getFeedbackQuestions().clear();
//        questionRepository.deleteAllByProject(project);
//
//        if (questions.length != types.length) {
//            throw new IllegalArgumentException("질문과 타입 배열의 길이가 일치하지 않습니다.");
//        }
//
//        handleQuestions(project, questions, types);
//
//    }

    private void handleQuestions(Project project, String[] questions, String[] types) {
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

    @Transactional
    public String deleteQuestion(Long questionId, String token) {

        Users user = getUsersByToken(token);

        Question question = questionRepository.findById(questionId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 질문"));

        if (!question.getProject().getUsers().equals(user)) {
            return "잘못된 사용자, 삭제 실패";
        }
        
        questionRepository.deleteById(questionId);

        return "질문 삭제 완료";
    }

    private Users getUsersByToken(String token) {
        Long userId = jwtTokenProvider.getUserIdFromJwt(token);
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 유저"));
        log.info("유저 정보: " + user.getNickname());
        return user;
    }

}
