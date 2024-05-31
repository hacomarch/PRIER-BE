package cocodas.prier.project.feedback.response;

import cocodas.prier.keywords.Keywords;
import cocodas.prier.project.feedback.question.Question;
import cocodas.prier.user.Users;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
public class Response {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
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
}
