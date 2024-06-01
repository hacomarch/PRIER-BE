package cocodas.prier.project.comment;

import cocodas.prier.project.project.Project;
import cocodas.prier.user.Users;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class ProjectComment {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;
    private String content;
    private Integer score;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "project_id")
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users users;
}
