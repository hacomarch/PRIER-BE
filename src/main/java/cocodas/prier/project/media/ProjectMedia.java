package cocodas.prier.project.media;

import cocodas.prier.project.project.Project;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class ProjectMedia {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectMediaId;

    @Column(nullable = false)
    private String metadata;

    @Column(nullable = false)
    private boolean isMain;

    @Column(nullable = false)
    private MediaType mediaType;

    @Column(nullable = false)
    private String s3Key;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;
}
