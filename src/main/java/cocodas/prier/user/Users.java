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
import java.util.Random;

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
    private Integer balance;

    // 카카오 로그인을 사용할 때 필요한 Builder
    @Builder
    public Users(String email, String nickname) {
        this.email = email;
        this.nickname = nickname;
        this.balance = 0;
    }

    // 마이페이지 수정을 위한 Setter
    public void updateNickName(String nickname) {
        this.nickname = nickname;
    }

    public void updateIntro(String intro) {
        this.intro = intro;
    }

    public void updateBelonging(String belonging) {
        this.belonging = belonging;
    }

    public void updateBlog(String blogUrl) {
        this.blogUrl = blogUrl;
    }

    public void updateGithub(String githubUrl) {
        this.githubUrl = githubUrl;
    }

    public void updateFigma(String figmaUrl) {
        this.figmaUrl = figmaUrl;
    }

    public void updateNotion(String notionUrl) {
        this.notionUrl = notionUrl;
    }

    public void updateLastLoginAt(LocalDateTime lastLoginAt) {
        this.lastLoginAt = lastLoginAt;
    }

    public void updateBalance(int amount) {
        if (this.balance == null) {
            this.balance = 0;
        }
        this.balance += amount;
    }

    // 테이블 연관관계 설정
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

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PointTransaction> pointTransaction = new ArrayList<>();

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Orders> orders = new ArrayList<>();

    @OneToMany(mappedBy = "users", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Quest> quests = new ArrayList<>();
}
