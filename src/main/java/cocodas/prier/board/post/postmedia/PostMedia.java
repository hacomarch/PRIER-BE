package cocodas.prier.board.post.postmedia;

import cocodas.prier.board.post.post.Post;
import cocodas.prier.project.media.MediaType;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class PostMedia {
    
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postMediaId;
    private String metadata;
    private LocalDateTime createdAt;
    private MediaType mediaType;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;
    
}
