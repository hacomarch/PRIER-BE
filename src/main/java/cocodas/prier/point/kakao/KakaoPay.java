package cocodas.prier.point.kakao;

import cocodas.prier.user.Users;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor
public class KakaoPay {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long KakaoPayId;

    private String tid;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private Users users;

    @Builder
    public KakaoPay(String tid, Users users) {
        this.tid = tid;
        this.users = users;
    }
}
