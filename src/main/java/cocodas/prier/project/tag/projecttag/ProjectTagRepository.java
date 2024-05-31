package cocodas.prier.project.tag.projecttag;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectTagRepository extends JpaRepository<ProjectTag, Long> {
}
