package cocodas.prier.project.media;

import cocodas.prier.project.project.Project;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class ProjectMedia {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long projectMediaId;
    private String metadata;
    private boolean isMain;

    private MediaType mediaType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;
}
