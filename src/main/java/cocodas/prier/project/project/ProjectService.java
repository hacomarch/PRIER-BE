package cocodas.prier.project.project;

import cocodas.prier.project.feedback.question.QuestionService;
import cocodas.prier.project.media.ProjectMediaService;
import cocodas.prier.project.project.dto.ProjectForm;
import cocodas.prier.project.tag.projecttag.ProjectTagService;
import cocodas.prier.user.UserRepository;
import cocodas.prier.user.Users;
import cocodas.prier.user.kakao.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectService {

    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;

    private final JwtTokenProvider jwtTokenProvider;
    private final ProjectTagService projectTagService;
    private final QuestionService questionService;
    private final ProjectMediaService projectMediaService;

    @Transactional
    public String createProject(ProjectForm form,
                                MultipartFile mainImage,
                                MultipartFile[] contentImages,
                                String token) {

        //유저 찾기
        Long userId = jwtTokenProvider.getUserIdFromJwt(token);
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 유저"));

        Project project = buildProject(form, user);
        project.setStatus(ProjectStatus.values()[form.getStatus()]);
        projectRepository.save(project);

        handleProjectTags(form, project);
        handleProjectQuestions(form, project);
        handleProjectMedia(mainImage, contentImages, project);

        return "프로젝트 생성 완료";
    }

    private Project buildProject(ProjectForm form, Users user) {
        Project project = Project.builder()
                .title(form.getTitle())
                .introduce(form.getIntroduce())
                .goal(form.getGoal())
                .devStartDate(form.getStartDate())
                .devEndDate(form.getEndDate())
                .link(form.getLink())
                .teamName(form.getTeamName())
                .teamDescription(form.getTeamDescription())
                .teamMate(form.getTeamMate())
                .users(user)
                .build();
        return project;
    }

    private void handleProjectTags(ProjectForm form, Project project) {
        if (form.getTags() != null) {
            projectTagService.linkTagsToProject(project, form.getTags());
        }
    }

    private void handleProjectQuestions(ProjectForm form, Project project) {
        if (form.getQuestion() != null && form.getType() != null) {
            questionService.createQuestion(project, form.getQuestion(), form.getType());
        }
    }

    private void handleProjectMedia(MultipartFile mainImage, MultipartFile[] contentImages, Project project) {
        if (mainImage != null) {
            try {
                projectMediaService.createMainImage(project, mainImage);
            } catch (IOException e) {
                log.error("메인 이미지 업로드 실패: {}", e.getMessage());
                throw new RuntimeException("메인 이미지 업로드 실패", e);
            }
        }
        if (contentImages != null) {
            try {
                projectMediaService.createContentImage(project, contentImages);
            } catch (IOException e) {
                log.error("내용 이미지 업로드 실패: {}", e.getMessage());
                throw new RuntimeException("내용 이미지 업로드 실패", e);
            }
        }
    }

    @Transactional
    public String deleteProject(Long projectId) {
        projectRepository.deleteById(projectId);
        return "프로젝트 삭제 완료";
    }

    @Transactional
    public String updateProject(Long projectId,
                                ProjectForm projectForm,
                                MultipartFile mainImage,
                                MultipartFile[] contentImages) {

        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 프로젝트"));


        project.setTitle(projectForm.getTitle());
        project.setIntroduce(projectForm.getIntroduce());
        project.setGoal(projectForm.getGoal());
        project.setDevStartDate(projectForm.getStartDate());
        project.setDevEndDate(projectForm.getEndDate());
        project.setStatus(ProjectStatus.values()[projectForm.getStatus()]);
        project.setTeamName(projectForm.getTeamName());
        project.setTeamDescription(projectForm.getTeamDescription());
        project.setTeamMate(projectForm.getTeamMate());
        project.setLink(projectForm.getLink());

        // 태그 업데이트
        if (projectForm.getTags() != null) {
            projectTagService.updateProjectTags(project, projectForm.getTags());
        }

        // 질문 업데이트
        if (projectForm.getQuestion() != null && projectForm.getType() != null) {
            questionService.updateQuestions(project, projectForm.getQuestion(), projectForm.getType());
        }

        // 미디어 파일 업데이트
        if (mainImage != null && !mainImage.isEmpty()) {
            try {
                projectMediaService.updateMainImage(project, mainImage);
            } catch (IOException e) {
                log.error("메인 이미지 업데이트 실패: {}", e.getMessage());
                throw new RuntimeException("메인 이미지 업데이트 실패", e);
            }
        }
        if (contentImages != null) {
            try {
                projectMediaService.updateContentImages(project, contentImages);
            } catch (IOException e) {
                log.error("내용 이미지 업데이트 실패: {}", e.getMessage());
                throw new RuntimeException("내용 이미지 업데이트 실패", e);
            }
        }

        return "프로젝트 업데이트 완료";
    }

}
