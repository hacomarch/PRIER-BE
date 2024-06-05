package cocodas.prier.quest;

import cocodas.prier.user.Users;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor
public class Quest {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questId;

    @Column(name = "first_quest")
    private Boolean first;

    @Column(name = "second_quest")
    private Boolean second;

    @Column(name = "third_quest")
    private Boolean third;
    private LocalDate createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users users;

    @Builder
    public Quest(boolean first, boolean second, boolean third, LocalDate createdAt) {
        this.first = first;
        this.second = second;
        this.third = third;
        this.createdAt = createdAt;
    }

    public void updateFirst(boolean first) {
        this.first = first;
    }

    public void updateSecond(boolean second) {
        this.second = second;
    }

    public void updateThird(boolean third) {
        this.third = third;
    }

    public void setUsers(Users users) {
        this.users = users;
        if (!users.getQuests().contains(this)) {
            users.getQuests().add(this);
        }
    }
}
