package cocodas.prier.project.feedback.response;

import cocodas.prier.project.comment.ProjectCommentService;
import cocodas.prier.project.feedback.question.Category;
import cocodas.prier.project.feedback.question.Question;
import cocodas.prier.project.feedback.question.QuestionRepository;
import cocodas.prier.project.feedback.response.dto.ResponseDetailDto;
import cocodas.prier.project.feedback.response.dto.ResponseDto;
import cocodas.prier.project.feedback.response.dto.ResponseObjectiveDto;
import cocodas.prier.project.feedback.response.dto.ResponseRequestDto;
import cocodas.prier.project.project.Project;
import cocodas.prier.project.project.ProjectRepository;
import cocodas.prier.project.project.ProjectService;
import cocodas.prier.statics.chatgpt.ChatGPTService;
import cocodas.prier.statics.chatgpt.response.SummaryResponse;
import cocodas.prier.statics.keywordAi.KeywordsService;
import cocodas.prier.statics.keywordAi.dto.response.KeyWordResponseDto;
import cocodas.prier.statics.objective.ObjectiveResponseService;
import cocodas.prier.user.Users;
import cocodas.prier.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ResponseService {

    private final ResponseRepository responseRepository;
    private final QuestionRepository questionRepository;
    private final UserRepository userRepository;
    private final ProjectRepository projectRepository;

    private final ProjectService projectService;
    private final KeywordsService keywordsService;
    private final ObjectiveResponseService objectiveResponseService;
    private final ChatGPTService chatGPTService;
    private final ProjectCommentService projectCommentService;

    @Transactional
    public List<ResponseDto> createResponses(Long userId, List<ResponseRequestDto> responsesDto) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

        List<Long> questionIds = responsesDto.stream()
                .map(ResponseRequestDto::getQuestionId)
                .collect(Collectors.toList());
        List<Question> questions = questionRepository.findAllById(questionIds);
        Map<Long, Question> questionMap = questions.stream()
                .collect(Collectors.toMap(Question::getQuestionId, Function.identity()));


        List<Response> responses = responsesDto.stream()
                .map(dto -> {
                    Question question = questionMap.get(dto.getQuestionId());
                    if (question == null) {
                        throw new IllegalArgumentException("Invalid question ID" + dto.getQuestionId());
                    }

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
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<ResponseDto> getResponsesByQuestion(Long questionId) {
        List<Response> responses = responseRepository.findAllByQuestionQuestionId(questionId);

        return responses.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public List<ResponseDto> getResponsesByProject(Long projectId) {
        List<Response> responses = responseRepository.findAllByQuestionProjectProjectId(projectId);

        return responses.stream()
                .map(this::mapToDto)
                .collect(Collectors.toList());
    }

    public ResponseDetailDto viewResponseDetail(Long projectId, Long userId) {

        Users users = userRepository.findById(userId).orElseThrow(() -> new IllegalArgumentException("사용자가 없습니다."));

        Project project = projectRepository.findById(projectId).orElseThrow(() -> new IllegalArgumentException("프로젝트가 없습니다"));

        List<KeyWordResponseDto> keywordByProjectId = keywordsService.getKeywordByProjectId(projectId);

        int[] feedbackAmount = projectService.getFeedbackAmount(project);

        Double percentage = objectiveResponseService.calculateFeedbackPercentage(projectId);

        List<SummaryResponse> chatGptResponse = chatGPTService.getChatGptResponse(projectId);

        List<ResponseObjectiveDto> objectiveByProject = getObjectiveByProject(projectId);

        return new ResponseDetailDto(
                projectId,
                project.getTitle(),
                project.getIntroduce(),
                project.getTeamName(),
                project.getLink(),
                keywordByProjectId,
                feedbackAmount[2],
                String.format("%.2f", percentage),
                chatGptResponse,
                objectiveByProject,
                projectCommentService.getProjectComments(userId),
                users.getBalance()
        );
    }

    public List<ResponseObjectiveDto> getObjectiveByProject(Long projectId) {
        List<Question> questions = projectRepository.findById(projectId)
                .map(project -> project.getFeedbackQuestions()
                        .stream()
                        .filter(question -> question.getCategory().equals(Category.OBJECTIVE))
                        .toList())
                .orElse(Collections.emptyList());

        List<ResponseObjectiveDto> objectiveCountList = new ArrayList<>();

        for (Question question : questions) {
            long veryGoodCount = question.getResponses().stream()
                    .filter(response -> response.getContent().equals("10"))
                    .count();
            long goodCount = question.getResponses().stream()
                    .filter(response -> response.getContent().equals("20"))
                    .count();
            long sosoCount = question.getResponses().stream()
                    .filter(response -> response.getContent().equals("30"))
                    .count();
            long badCount = question.getResponses().stream()
                    .filter(response -> response.getContent().equals("40"))
                    .count();
            long veryBadCount = question.getResponses().stream()
                    .filter(response -> response.getContent().equals("50"))
                    .count();

            ResponseObjectiveDto dto = new ResponseObjectiveDto(
                    question.getQuestionId(),
                    (int) veryGoodCount,
                    (int) goodCount,
                    (int) sosoCount,
                    (int) badCount,
                    (int) veryBadCount
            );

            objectiveCountList.add(dto);
        }
        return objectiveCountList;
    }

    @Transactional
    public void deleteResponses(Long projectId, Long userId) {
        List<Response> responses = responseRepository.findAllByQuestionProjectProjectIdAndUsersUserId(projectId, userId);
        responseRepository.deleteAll(responses);
    }

    public List<Long> getProjectsByUser(Long userId) {
        List<Long> projectIds = responseRepository.findDistinctProjectIdsByUserId(userId);
        return projectIds;
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
