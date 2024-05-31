package cocodas.prier.project.project;

import cocodas.prier.project.comment.ProjectComment;
import cocodas.prier.project.feedback.question.Question;
import cocodas.prier.project.media.ProjectMedia;
import cocodas.prier.project.tag.projecttag.ProjectTag;
import cocodas.prier.user.Users;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Project {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long projectId;
    private String title;
    private LocalDateTime createdAt;
    private LocalDateTime feedbackEndAt;
    private LocalDateTime updatedAt;
    private ProjectStatus status;
    private String teamName;
    private String teamDescription;
    private String link;
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    @ManyToOne(fetch = FetchType.LAZY)
    private Users users;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectComment> projectComments = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectTag> projectTags = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectMedia> projectMedia = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Question> feedbackQuestions = new ArrayList<>();
}
