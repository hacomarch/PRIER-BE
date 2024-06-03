package cocodas.prier.user;

import cocodas.prier.board.comment.PostComment;
import cocodas.prier.board.post.post.Post;
import cocodas.prier.orders.orders.Orders;
import cocodas.prier.point.PointTransaction;
import cocodas.prier.project.feedback.response.Response;
import cocodas.prier.project.project.Project;
import cocodas.prier.project.comment.ProjectComment;
import cocodas.prier.quest.Quest;
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
public class Users {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long userId;
    private String email;
    private String nickname;
    private String intro;
    private String belonging;
    private Rank tier;
    private String blogUrl;
    private String githubUrl;
    private String figmaUrl;
    private String notionUrl;
    private LocalDateTime lastLoginAt;

    @Builder
    public Users(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;
    }

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Post> posts = new ArrayList<>();

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PostComment> postComments = new ArrayList<>();

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Project> projects = new ArrayList<>();

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ProjectComment> projectComments = new ArrayList<>();

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Response> responses = new ArrayList<>();

    @OneToOne(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
    private PointTransaction pointTransaction;

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Orders> orders = new ArrayList<>();

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Quest> quests = new ArrayList<>();


}
