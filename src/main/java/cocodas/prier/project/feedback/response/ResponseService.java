package cocodas.prier.project.feedback.response;

import cocodas.prier.project.feedback.question.Question;
import cocodas.prier.project.feedback.question.QuestionRepository;
import cocodas.prier.project.feedback.response.dto.ResponseDto;
import cocodas.prier.project.feedback.response.dto.ResponseRequestDto;
import cocodas.prier.user.Users;
import cocodas.prier.user.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Slf4j
public class ResponseService {

    private final ResponseRepository responseRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;

    private static final String INVALID_USER_ID_MESSAGE = "Invalid user ID: ";
    private static final String INVALID_QUESTION_ID_MESSAGE = "Invalid question ID: ";

    @Transactional
    public List<ResponseDto> createResponses(Long userId, List<ResponseRequestDto> responsesDto) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(INVALID_USER_ID_MESSAGE + userId));

        List<Long> questionIds = responsesDto.stream()
                .map(ResponseRequestDto::getQuestionId)
                .collect(Collectors.toList());
        List<Question> questions = questionRepository.findAllById(questionIds);
        Map<Long, Question> questionMap = questions.stream()
                .collect(Collectors.toMap(Question::getQuestionId, Function.identity()));


        List<Response> responses = responsesDto.stream()
                .map(dto -> {
                    Question question = Optional.ofNullable(questionMap.get(dto.getQuestionId()))
                            .orElseThrow(() -> new IllegalArgumentException(INVALID_QUESTION_ID_MESSAGE + dto.getQuestionId()));

                    return Response.builder()
                            .content(dto.getContent())
                            .createdAt(LocalDateTime.now())
                            .question(question)
                            .user(user)
                            .build();
                })
                .collect(Collectors.toList());

        responseRepository.saveAll(responses);
        log.info("Responses saved for user Id: {}", userId);

        return responses.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

//    public List<ResponseDto> getResponsesByQuestion(Long questionId) {
//        List<Response> responses = responseRepository.findAllByQuestionQuestionId(questionId);
//
//        return responses.stream()
//                .map(this::mapToDto)
//                .collect(Collectors.toList());
//    }

    public List<ResponseDto> getResponsesByProject(Long projectId) {
        List<Response> responses = responseRepository.findAllByQuestionProjectProjectId(projectId);
        log.info("Response for project ID: {}", projectId);
        return responses.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteResponses(Long projectId, Long userId) {
        List<Response> responses = responseRepository.findAllByQuestionProjectProjectIdAndUsersUserId(projectId, userId);
        responseRepository.deleteAll(responses);
        log.info("Responses deleted for project ID: {} and user ID: {}", projectId, userId);
    }

    public List<Long> getProjectsByUser(Long userId) {
        List<Long> projectIds = responseRepository.findDistinctProjectIdsByUserId(userId);
        log.info("Project IDs retrieved for user ID: {}", userId);
        return projectIds;
    }

    public long countFeedbackForUserProjectsAfterLastLogin(Long userId) {
        LocalDateTime lastLoginAt = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException(INVALID_USER_ID_MESSAGE + userId))
                .getLastLoginAt();
        long feedbackCount = responseRepository.countFeedbackForUserProjectsAfterLastLogin(userId, lastLoginAt);
        log.info("Feedback count after last login for user ID: {} is {}", userId, feedbackCount);
        return feedbackCount;
    }

    private ResponseDto mapToDto(Response response) {
        return ResponseDto.builder()
                .responseId(response.getResponseId())
                .content(response.getContent())
                .createdAt(response.getCreatedAt().toString())
                .questionId(response.getQuestion().getQuestionId())
                .userId(response.getUsers().getUserId())
                .build();
    }
}
