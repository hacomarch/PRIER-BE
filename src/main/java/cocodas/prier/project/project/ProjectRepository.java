package cocodas.prier.project.project;

import cocodas.prier.user.Users;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long>, JpaSpecificationExecutor<Project> {

    @Query("SELECT DISTINCT p FROM Project p " +
            "LEFT JOIN p.projectTags pt " +
            "LEFT JOIN pt.tag t " +
            "WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.teamName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(t.tagName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Project> findByKeyword(@Param("keyword") String keyword, Pageable pageable);

    @Query("SELECT p FROM Project p " +
            "WHERE p.users = :user " +
            "ORDER BY p.createdAt DESC " +
            "LIMIT 1")
    Optional<Project> findMyRecentProject(@Param("user") Users user);
}
