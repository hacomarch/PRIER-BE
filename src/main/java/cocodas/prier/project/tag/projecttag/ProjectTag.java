package cocodas.prier.project.tag.projecttag;

import cocodas.prier.project.project.Project;
import cocodas.prier.project.tag.tag.Tag;
import jakarta.persistence.*;
import lombok.Getter;

@Entity
@Getter
public class ProjectTag {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long projectTagId;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id")
    private Tag tag;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;
}
