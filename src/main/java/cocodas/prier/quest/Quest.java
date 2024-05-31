package cocodas.prier.quest;

import cocodas.prier.user.Users;
import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class Quest {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long questId;
    private Integer sequence;
    private QuestStatus questStatus;
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    private Users users;
}
