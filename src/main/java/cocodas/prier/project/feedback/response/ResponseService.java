package cocodas.prier.project.feedback.response;

import cocodas.prier.project.feedback.question.Question;
import cocodas.prier.project.feedback.question.QuestionRepository;
import cocodas.prier.project.feedback.response.dto.ResponseDto;
import cocodas.prier.project.feedback.response.dto.ResponseRequestDto;
import cocodas.prier.project.project.Project;
import cocodas.prier.project.project.ProjectRepository; // ProjectRepository import
import cocodas.prier.project.project.dto.ProjectDto;
import cocodas.prier.project.tag.tag.dto.TagDto;
import cocodas.prier.user.Users;
import cocodas.prier.user.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ResponseService {

    private final ResponseRepository responseRepository;
    private final QuestionRepository questionRepository;
    private final ProjectRepository projectRepository; // ProjectRepository 추가
    private final UserRepository userRepository;

    @Transactional
    public List<ResponseDto> createResponses(Long userId, Long projectId, List<ResponseRequestDto> responsesDto) {
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid user ID"));

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new IllegalArgumentException("Invalid project ID: " + projectId));

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
                        throw new IllegalArgumentException("Invalid question ID: " + dto.getQuestionId());
                    }

                    return Response.builder()
                            .content(dto.getContent())
                            .createdAt(LocalDateTime.now())
                            .question(question)
                            .user(user)
                            .project(project)
                            .build();
                })
                .collect(Collectors.toList());

        responseRepository.saveAll(responses);

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

    public List<ProjectDto> getRespondedProjectsByUser(Long userId) {
        List<Project> projects = responseRepository.findDistinctProjectsByUserId(userId);

        return projects.stream()
                .map(this::mapToProjectDto)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteResponses(Long projectId, Long userId) {
        List<Response> responses = responseRepository.findAllByQuestionProjectProjectIdAndUsersUserId(projectId, userId);
        responseRepository.deleteAll(responses);
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

    private ProjectDto mapToProjectDto(Project project) {
        List<TagDto> tagDtos = project.getProjectTags().stream()
                .map(projectTag -> new TagDto(projectTag.getTag().getTagId(), projectTag.getTag().getTagName()))
                .collect(Collectors.toList());

        return ProjectDto.builder()
                .projectId(project.getProjectId())
                .title(project.getTitle())
                .teamName(project.getTeamName())
                .mainImageUrl(project.getProjectMedia().isEmpty() ? null : project.getProjectMedia().get(0).getS3Key()) // 예시로 첫 번째 미디어 URL 사용
                .tags(tagDtos)
                .score(project.getScore())
                .build();
    }
}
