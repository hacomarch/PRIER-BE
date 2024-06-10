package cocodas.prier.project.feedback.response;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ResponseRepository extends JpaRepository<Response, Long> {
    List<Response> findAllByQuestionQuestionId(Long questionId);
    List<Response> findAllByQuestionProjectProjectId(Long projectId);
    List<Response> findAllByQuestionProjectProjectIdAndUsersUserId(Long projectId, Long userId);
}
