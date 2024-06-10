package cocodas.prier.project.feedback.response;

import cocodas.prier.project.feedback.question.Question;
import cocodas.prier.project.feedback.question.QuestionRepository;
import cocodas.prier.project.feedback.response.dto.ResponseDto;
import cocodas.prier.project.feedback.response.dto.ResponseRequestDto;
import cocodas.prier.user.Users;
import cocodas.prier.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ResponseService {

    private final ResponseRepository responseRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;

    @Transactional
    public List<ResponseDto> createResponses(Long userId, List<ResponseRequestDto> responsesDto) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

        List<Response> responses = responsesDto.stream()
                .map(dto -> {
                    Question question = questionRepository.findById(dto.getQuestionId())
                            .orElseThrow(() -> new IllegalArgumentException("Invalid question ID"));

                    return Response.builder()
                            .content(dto.getContent())
                            .createdAt(LocalDateTime.now())
                            .question(question)
                            .user(user)
                            .build();
                })
                .collect(Collectors.toList());

        responseRepository.saveAll(responses);

        return responses.stream()
                .map(response -> ResponseDto.builder()
                        .responseId(response.getResponseId())
                        .content(response.getContent())
                        .createdAt(response.getCreatedAt().toString())
                        .questionId(response.getQuestion().getQuestionId())
                        .userId(response.getUsers().getUserId())
                        .build())
                .collect(Collectors.toList());
    }

    public List<ResponseDto> getResponsesByQuestion(Long questionId) {
        List<Response> responses = responseRepository.findAllByQuestionQuestionId(questionId);

        return responses.stream()
                .map(response -> ResponseDto.builder()
                        .responseId(response.getResponseId())
                        .content(response.getContent())
                        .createdAt(response.getCreatedAt().toString())
                        .questionId(response.getQuestion().getQuestionId())
                        .userId(response.getUsers().getUserId())
                        .build())
                .collect(Collectors.toList());
    }

    public List<ResponseDto> getResponsesByProject(Long projectId) {
        List<Response> responses = responseRepository.findAllByQuestionProjectProjectId(projectId);

        return responses.stream()
                .map(response -> ResponseDto.builder()
                        .responseId(response.getResponseId())
                        .content(response.getContent())
                        .createdAt(response.getCreatedAt().toString())
                        .questionId(response.getQuestion().getQuestionId())
                        .userId(response.getUsers().getUserId())
                        .build())
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteResponsesByUserAndProject(Long projectId, Long userId) {
        List<Response> responses = responseRepository.findAllByQuestionProjectProjectIdAndUsersUserId(projectId, userId);
        responseRepository.deleteAll(responses);
    }
}
