package cocodas.prier.project.tag.projecttag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProjectTagRepository extends JpaRepository<ProjectTag, Long> {

    @Query("SELECT pt FROM ProjectTag pt JOIN FETCH pt.project WHERE pt.tag.tagId = :tagId")
    List<ProjectTag> findByTagIdWithProject(Long tagId);

    List<ProjectTag> findByProjectProjectId(Long projectId);
}
