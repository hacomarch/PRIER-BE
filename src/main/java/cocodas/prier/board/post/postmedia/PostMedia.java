package cocodas.prier.board.post.postmedia;

import cocodas.prier.board.post.post.Post;
import cocodas.prier.project.media.MediaType;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class PostMedia {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long postMediaId;
    private String metadata;
    private LocalDateTime createdAt;
    private String s3Key;

    @Enumerated(EnumType.STRING)
    private MediaType mediaType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @Builder
    public PostMedia(String metadata, String s3Key, MediaType mediaType, Post post) {
        this.metadata = metadata;
        this.createdAt = LocalDateTime.now();
        this.s3Key = s3Key;
        this.mediaType = mediaType;
        this.post = post;
        if (!post.getPostMedia().contains(this)) {
            post.getPostMedia().add(this);
        }
    }


}
