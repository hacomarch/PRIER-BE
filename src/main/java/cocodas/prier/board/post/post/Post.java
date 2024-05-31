package cocodas.prier.board.post.post;

import cocodas.prier.board.comment.PostComment;
import cocodas.prier.board.post.postmedia.PostMedia;
import cocodas.prier.user.Users;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
public class Post {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long postId;
    private String title;
    private String content;
    private String category;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private Users users;

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostComment> postComments = new ArrayList<>();

    @OneToMany(mappedBy = "post", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostMedia> postMedia = new ArrayList<>();
}
