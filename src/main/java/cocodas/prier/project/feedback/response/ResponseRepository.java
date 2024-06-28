package cocodas.prier.project.feedback.response;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface ResponseRepository extends JpaRepository<Response, Long> {
    List<Response> findAllByQuestionQuestionId(Long questionId);
    List<Response> findAllByQuestionProjectProjectId(Long projectId);
    List<Response> findAllByUsers_UserId(Long userId);
    List<Response> findAllByQuestionProjectProjectIdAndUsersUserId(Long projectId, Long userId);
    @Query("SELECT r FROM Response r JOIN FETCH r.question q WHERE q.questionId IN :questionIds")
    List<Response> findAllByQuestionIds(@Param("questionIds") List<Long> questionIds);

    @Query("SELECT DISTINCT r.question.project.projectId FROM Response r WHERE r.users.userId = :userId")
    List<Long> findDistinctProjectIdsByUserId(@Param("userId") Long userId);

    @Query("SELECT COUNT(DISTINCT p.projectId) " +
            "FROM Response r " +
            "JOIN r.question q " +
            "JOIN q.project p " +
            "WHERE p.users.userId = :userId " +
            "AND r.createdAt > :lastLogoutAt")
    long countFeedbackForUserProjectsAfterLastLogout(@Param("userId") Long userId, @Param("lastLogoutAt") LocalDateTime lastLogoutAt);


}

