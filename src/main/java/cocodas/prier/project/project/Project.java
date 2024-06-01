package cocodas.prier.project.project;

import cocodas.prier.project.comment.ProjectComment;
import cocodas.prier.project.feedback.question.Question;
import cocodas.prier.project.media.ProjectMedia;
import cocodas.prier.project.tag.projecttag.ProjectTag;
import cocodas.prier.user.Users;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Project {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long projectId;

    @Column(nullable = false)
    private String title;

    @Column(columnDefinition = "TEXT")
    private String introduce;

    @Column(columnDefinition = "TEXT")
    private String goal;

    @Column(nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();

    // 게시일은 2주뒤까지로
    @Column(nullable = false)
    private LocalDateTime feedbackEndAt = LocalDateTime.now().plus(2, ChronoUnit.WEEKS);

    private LocalDateTime updatedAt;

    @Column(nullable = false)
    private ProjectStatus status;

    @Column(nullable = false)
    private String teamName;

    @Column(columnDefinition = "TEXT")
    private String teamDescription;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String link;

    @Column(nullable = false)
    private LocalDateTime devStartDate;

    @Column(nullable = false)
    private LocalDateTime devEndDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
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
