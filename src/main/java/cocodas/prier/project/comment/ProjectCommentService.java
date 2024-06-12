package cocodas.prier.project.comment;

import cocodas.prier.project.comment.dto.CommentDto;
import cocodas.prier.project.comment.dto.CommentForm;
import cocodas.prier.project.comment.dto.MyPageCommentDto;
import cocodas.prier.project.project.Project;
import cocodas.prier.project.project.ProjectRepository;
import cocodas.prier.user.UserRepository;
import cocodas.prier.user.Users;
import cocodas.prier.user.kakao.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectCommentService {

    private final ProjectCommentRepository projectCommentRepository;
    private final ProjectRepository projectRepository;
    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;


    private Users getUsersByToken(String token) {
        Long userId = jwtTokenProvider.getUserIdFromJwt(token);
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 유저"));
    }

    //todo: 퀘스트랑 연결해야할 것 같음
    @Transactional
    public CommentDto createProjectComment(Long projectId, CommentForm form, String token) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 프로젝트"));

        Users user = getUsersByToken(token);

        ProjectComment comment = ProjectComment.builder()
                .users(user)
                .project(project)
                .content(form.getComment())
                .score(form.getScore())
                .build();

        projectCommentRepository.save(comment);

        user.getProjectComments().add(comment);
        project.getProjectComments().add(comment);
        project.updateScore(form.getScore());
        log.info("댓글 등록 성공");
        return new CommentDto(comment.getCommentId(), comment.getUsers().getNickname(), comment.getContent(), comment.getScore());
    }

    @Transactional
    public String deleteProjectComment(Long projectId, Long commentId, String token) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 프로젝트"));

        ProjectComment comment = projectCommentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 댓글"));

        Users user = getUsersByToken(token);
        if (!user.equals(comment.getUsers())) {
            return "잘못된 사용자, 댓글 삭제 실패";
        }

        project.updateScore(-comment.getScore());
        projectCommentRepository.deleteById(commentId);
        return "댓글 삭제 성공";
    }

    public List<CommentDto> getProjectComments(Long projectId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 프로젝트"));

        List<ProjectComment> allComments = projectCommentRepository.findAllByProject(project);

        return allComments.stream().map(comment -> new CommentDto(
                        comment.getCommentId(),
                        comment.getUsers().getNickname(),
                        comment.getContent(),
                        comment.getScore())).collect(Collectors.toList());
    }

    public List<MyPageCommentDto> getMyProjectComments(String token) {
        Users user = getUsersByToken(token);

        List<ProjectComment> allComments = projectCommentRepository.findAllByUsers(user);

        return allComments.stream().map(comment -> new MyPageCommentDto(
                comment.getCommentId(),
                comment.getProject().getTitle(),
                comment.getProject().getTeamName(),
                comment.getContent(),
                comment.getScore())).collect(Collectors.toList());
    }

    @Transactional
    public CommentDto updateProjectComment(Long projectId, Long commentId, CommentForm form, String token) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 프로젝트"));

        ProjectComment comment = projectCommentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 댓글"));

        if (!project.equals(comment.getProject())) {
            log.info("잘못된 접근, 댓글 수정 실패");
            return null;
        }

        Users user = getUsersByToken(token);
        if (!user.equals(comment.getUsers())) {
            log.info("잘못된 사용자, 댓글 수정 실패");
            return null;
        }

        comment.setContent(form.getComment());
        project.updateScore(-comment.getScore());
        comment.setScore(form.getScore());
        project.updateScore(form.getScore());
        comment.setUpdatedAt(LocalDateTime.now());

        log.info("댓글 수정 완료");
        return new CommentDto(comment.getCommentId(), comment.getUsers().getNickname(), comment.getContent(), comment.getScore());
    }
}
