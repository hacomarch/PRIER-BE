package cocodas.prier.project.comment;

import cocodas.prier.project.project.Project;
import cocodas.prier.user.Users;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class ProjectComment {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long commentId;
    private String content;
    private Integer score;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private Project project;

    @ManyToOne(fetch = FetchType.LAZY)
    private Users users;
}
