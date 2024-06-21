package cocodas.prier.project.comment;

import cocodas.prier.project.project.Project;
import cocodas.prier.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectCommentRepository extends JpaRepository<ProjectComment, Long> {
    List<ProjectComment> findAllByProject(Project project);
    List<ProjectComment> findAllByUsers(Users user);

    @Query("SELECT pc.project.projectId, COUNT(pc) FROM ProjectComment pc " +
            "JOIN Project p ON p.projectId = pc.project.projectId " +
            "JOIN Users u ON u.userId = p.users.userId " +
            "WHERE pc.createdAt > u.lastLoginAt " +
            "AND u.userId = :userId " +
            "GROUP BY pc.project.projectId")
    List<Object[]> countCommentsAfterLastLogin(@Param("userId") Long userId);
}
