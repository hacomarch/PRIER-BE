package cocodas.prier.project.media;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProjectMediaRepository extends JpaRepository<ProjectMedia, Long> {
}
