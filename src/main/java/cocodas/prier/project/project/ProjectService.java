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

//    @Transactional
//    public String createProject(CreateProjectForm form,
//                                MultipartFile mainImage,
//                                MultipartFile[] contentImages,
//                                String token) {
//
//        //유저 찾기
//        Long userId = jwtTokenProvider.getUserIdFromJwt(token);
//        Users user = userRepository.findById(userId)
//                .orElseThrow(() -> new RuntimeException("존재하지 않는 유저"));
//
//        Project project = Project.builder()
//                .title(form.getTitle())
//                .introduce(form.getIntroduce())
//                .goal(form.getGoal())
//                .teamName(form.getTeamName())
//                .teamDescription(form.getTeamDescription())
//                .teamMate(form.getTeamMate())
//                .users(user)
//                .build();
//
//        //status 처리
//        switch (form.getStatus()) {
//            case 0:
//                project.setStatus(ProjectStatus.PLANNING);
//                break;
//            case 1:
//                project.setStatus(ProjectStatus.DEVELOPING);
//                break;
//            case 2:
//                project.setStatus(ProjectStatus.DEPLOYMENT_COMPLETE);
//                break;
//        }
//
//        projectRepository.save(project);
//
//        //tag 처리
//        if (form.getTags() != null) {
//            projectTagService.linkTagsToProject(project, form.getTags());
//        }
//
//        // 질문 처리
//        if (form.getQuestion() != null && form.getType() != null) {
//            questionService.createQuestion(project, form.getQuestion(), form.getType());
//        }
//
//        //미디어 처리
//        if (mainImage != null) {
//            try {
//                String result = projectMediaService.createMainImage(project, mainImage);
//                log.info(result);
//            } catch (IOException e) {
//                log.info("메인 이미지 등록 실패 = {}", e.getMessage());
//            }
//        }
//
//        if (contentImages != null) {
//            try {
//                String result = projectMediaService.createContentImage(project, contentImages);
//                log.info(result);
//            } catch (IOException e) {
//                log.info("내용 이미지 등록 실패 = {}", e.getMessage());
//            }
//        }
//
//
//        return "프로젝트 생성 완료";
//
//    }

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
        handleProjectStatus(form.getStatus(), project);
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

    private void handleProjectStatus(int status, Project project) {
        switch (status) {
            case 0:
                project.setStatus(ProjectStatus.PLANNING);
                break;
            case 1:
                project.setStatus(ProjectStatus.DEVELOPING);
                break;
            case 2:
                project.setStatus(ProjectStatus.DEPLOYMENT_COMPLETE);
                break;
        }
    }

}
