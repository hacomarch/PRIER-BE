package cocodas.prier.board.post.postmedia;

import cocodas.prier.board.post.post.Post;
import cocodas.prier.project.media.MediaType;
import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
public class PostMedia {
    
    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long postMediaId;
    private String metadata;
    private LocalDateTime createdAt;
    private MediaType mediaType;
    
    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;
    
}
