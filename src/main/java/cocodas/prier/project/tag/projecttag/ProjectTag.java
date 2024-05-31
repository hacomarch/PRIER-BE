package cocodas.prier.project.tag.projecttag;

import cocodas.prier.project.project.Project;
import cocodas.prier.project.tag.tag.Tag;
import jakarta.persistence.*;

@Entity
public class ProjectTag {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long projectTagId;

    @ManyToOne(fetch = FetchType.LAZY)
    private Tag tag;

    @ManyToOne(fetch = FetchType.LAZY)
    private Project project;
}
