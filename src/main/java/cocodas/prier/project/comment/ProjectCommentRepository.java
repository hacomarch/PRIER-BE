package cocodas.prier.project.comment;

import cocodas.prier.project.project.Project;
import cocodas.prier.user.Users;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ProjectCommentRepository extends JpaRepository<ProjectComment, Long> {
    List<ProjectComment> findAllByProject(Project project);
    List<ProjectComment> findAllByUsers(Users user);


    @Query("SELECT COUNT(pc) " +
            "FROM ProjectComment pc " +
            "JOIN pc.project p " +
            "WHERE p.users.userId = :userId " +
            "AND pc.createdAt > :lastLogoutAt")
    long countProjectCommentsForUserAfterLastLogout(@Param("userId") Long userId, @Param("lastLogoutAt") LocalDateTime lastLogoutAt);

    List<ProjectComment> findByUsers_UserId(Long userId);
}
