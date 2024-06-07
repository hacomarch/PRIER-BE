package cocodas.prier.project.project;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectRepository extends JpaRepository<Project, Long> {

    @Query("SELECT DISTINCT p FROM Project p " +
            "LEFT JOIN p.projectTags pt " +
            "LEFT JOIN pt.tag t " +
            "WHERE LOWER(p.title) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.teamName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(t.tagName) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    List<Project> findByKeyword(@Param("keyword") String keyword);
}
