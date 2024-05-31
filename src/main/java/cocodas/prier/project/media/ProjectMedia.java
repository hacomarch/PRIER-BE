package cocodas.prier.project.media;

import cocodas.prier.project.project.Project;
import jakarta.persistence.*;

@Entity
public class ProjectMedia {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long projectMediaId;
    private String metadata;
    private boolean isMain;

    private MediaType mediaType;

    @ManyToOne(fetch = FetchType.LAZY)
    private Project project;
}
