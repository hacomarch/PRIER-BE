package cocodas.prier.project.comment;

import cocodas.prier.project.project.Project;
import cocodas.prier.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectCommentRepository extends JpaRepository<ProjectComment, Long> {
    List<ProjectComment> findAllByProject(Project project);
    List<ProjectComment> findAllByUsers(Users user);
}
