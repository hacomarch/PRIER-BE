package cocodas.prier.project.feedback.response;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResponseRepository extends JpaRepository<Response, Long> {
    List<Response> findAllByQuestionQuestionId(Long questionId);
    List<Response> findAllByQuestionProjectProjectId(Long projectId);
    List<Response> findAllByQuestionProjectProjectIdAndUsersUserId(Long projectId, Long userId);
    @Query("SELECT r FROM Response r JOIN FETCH r.question q WHERE q.questionId IN :questionIds")
    List<Response> findAllByQuestionIds(@Param("questionIds") List<Long> questionIds);
}

