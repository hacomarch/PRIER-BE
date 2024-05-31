package cocodas.prier.board.comment;

import cocodas.prier.board.post.post.Post;
import cocodas.prier.user.Users;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class PostComment {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long commentId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    private Users users;
}
