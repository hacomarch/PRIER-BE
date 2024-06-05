package cocodas.prier.project.media;

import cocodas.prier.project.project.Project;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class ProjectMedia {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectMediaId;

    @Column(nullable = false)
    private String metadata;

    @Column(nullable = false)
    private boolean isMain;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private MediaType mediaType = MediaType.IMAGE;

    @Column(nullable = false)
    private String s3Key;

    private int orderIndex;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @Builder
    public ProjectMedia(String metadata, boolean isMain, String s3Key, int orderIndex, Project project) {
        this.metadata = metadata;
        this.isMain = isMain;
        this.s3Key = s3Key;
        this.orderIndex = orderIndex;
        this.project = project;
    }
    public void setOrderIndex(int order) {
        this.orderIndex = order;
    }

    public void setMetadata(String metadata) {
        this.metadata = metadata;
    }

    public void setS3Key(String s3Key) {
        this.s3Key = s3Key;
    }
}
