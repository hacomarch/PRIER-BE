package cocodas.prier.project.project;

import cocodas.prier.project.comment.ProjectComment;
import cocodas.prier.project.feedback.question.Question;
import cocodas.prier.project.media.ProjectMedia;
import cocodas.prier.project.tag.projecttag.ProjectTag;
import cocodas.prier.user.Users;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
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
    private LocalDateTime feedbackEndAt = LocalDateTime.now().plusWeeks(2);

    private LocalDateTime updatedAt;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private ProjectStatus status;

    @Column(nullable = false)
    private String teamName;

    @Column(columnDefinition = "TEXT")
    private String teamDescription;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String link;

    @Column(nullable = false)
    private LocalDate devStartDate;

    @Column(nullable = false)
    private LocalDate devEndDate;

    private String teamMate;

    @Column(nullable = false)
    private Float score = 0F;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users users;

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectComment> projectComments = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectTag> projectTags = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderColumn(name = "orderIndex")
    private List<ProjectMedia> projectMedia = new ArrayList<>();

    @OneToMany(mappedBy = "project", cascade = CascadeType.ALL, orphanRemoval = true)
    @OrderColumn(name = "orderIndex")
    private List<Question> feedbackQuestions = new ArrayList<>();

    @Builder
    public Project(String title, String introduce, String goal, String teamName, String teamDescription, String link, LocalDate devStartDate, LocalDate devEndDate, String teamMate, Users users) {
        this.title = title;
        this.introduce = introduce;
        this.goal = goal;
        this.teamName = teamName;
        this.teamDescription = teamDescription;
        this.link = link;
        this.devStartDate = devStartDate;
        this.devEndDate = devEndDate;
        this.teamMate = teamMate;
        this.users = users;
    }

    //피드백 기간 연장 메소드
    public void addFeedbackEndAt(int week) {
        this.feedbackEndAt = this.feedbackEndAt.plusWeeks(week);
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public void setGoal(String goal) {
        this.goal = goal;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public void setStatus(ProjectStatus status) {
        this.status = status;
    }

    public void setTeamName(String teamName) {
        this.teamName = teamName;
    }

    public void setTeamDescription(String teamDescription) {
        this.teamDescription = teamDescription;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public void setDevStartDate(LocalDate devStartDate) {
        this.devStartDate = devStartDate;
    }

    public void setDevEndDate(LocalDate devEndDate) {
        this.devEndDate = devEndDate;
    }

    public void setTeamMate(String teamMate) {
        this.teamMate = teamMate;
    }

    public void updateScore(Float score) {
        this.score += score;
    }
}
