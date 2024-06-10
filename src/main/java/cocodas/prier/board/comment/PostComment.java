package cocodas.prier.board.comment;

import cocodas.prier.board.post.post.Post;
import cocodas.prier.user.Users;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor
public class PostComment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long commentId;
    private String content;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "post_id")
    private Post post;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users users;

    public void updateContent(String content) {
        this.content = content;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = LocalDateTime.now();
    }

    @Builder
    public PostComment(String content, Users users, Post post) {
        this.content = content;
        this.createdAt = LocalDateTime.now();
        this.users = users;
        if (!users.getPostComments().contains(this)) {
            users.getPostComments().add(this);
        }
        this.post = post;
        if (!post.getPostComments().contains(this)) {
            post.getPostComments().add(this);
        }
    }
}
