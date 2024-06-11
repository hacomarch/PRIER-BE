package cocodas.prier.project.feedback.response;

import cocodas.prier.keywords.Keywords;
import cocodas.prier.project.feedback.question.Question;
import cocodas.prier.user.Users;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor
public class Response {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long responseId;
    private String content;
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id")
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users users;

    @OneToMany(mappedBy = "response", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Keywords> keywords = new ArrayList<>();

    @Builder
    public Response(String content, LocalDateTime createdAt,Question question, Users user) {
        this.content = content;
        this.createdAt = createdAt;
        this.question = question;
        this.users = user;
    }
}
