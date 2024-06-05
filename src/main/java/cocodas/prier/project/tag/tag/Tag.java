package cocodas.prier.project.tag.tag;

import cocodas.prier.project.tag.projecttag.ProjectTag;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Tag {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long tagId;

    private String tagName;

    @OneToMany(mappedBy = "tag", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectTag> projectTags = new ArrayList<>();

    public Tag(String tagName) {
        this.tagName = tagName;
    }

    public void addProjectTags(ProjectTag projectTags) {
        this.projectTags.add(projectTags);
    }
}
