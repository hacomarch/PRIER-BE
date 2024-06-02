package cocodas.prier.keywords;

import cocodas.prier.project.feedback.response.Response;
import jakarta.persistence.*;

@Entity
public class Keywords {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long keywordId;
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "response_id")
    private Response response;

}
