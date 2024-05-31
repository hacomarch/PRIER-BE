package cocodas.prier.project.tag.tag;

import cocodas.prier.project.tag.projecttag.ProjectTag;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
public class Tag {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long tagId;

    private String tagName;

    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectTag> projectTags = new ArrayList<>();
}
