package cocodas.prier.quest;

import cocodas.prier.user.Users;
import jakarta.persistence.*;
import lombok.Getter;

import java.time.LocalDateTime;

@Entity
@Getter
public class Quest {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long questId;
    private Integer sequence;
    private QuestStatus questStatus;
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users users;
}
