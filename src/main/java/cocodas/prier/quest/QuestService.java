package cocodas.prier.quest;

import cocodas.prier.user.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuestService {
    private final QuestRepository questRepository;

    //TODO : UserService에서 회원 create 할 때 이 메서드도 불러야 함
    @Transactional
    public void createQuest(Users users) {
        Quest quest = Quest.builder()
                .first(false)
                .second(false)
                .third(false)
                .createdAt(LocalDate.now(ZoneId.of("Asia/Seoul")))
                .build();
        quest.setUsers(users);

        questRepository.save(quest);
    }

    @Transactional
    public void updateQuest(LocalDate createdAt, Long userId, int sequence) {
        Quest quest = findByCreatedAtAndUserId(createdAt, userId);
        if (sequence == 1) {
            quest.updateFirst(true);
        } else if (sequence == 2) {
            //TODO : 오늘치 나의 댓글이 있고 && first가 완료되었으면 updateSecond(true), 없으면 예외 처리
        } else if (sequence == 3) {
            //TODO : 오늘치 나의 피드백이 있고 && second가 완료되었으면 updateThird(true), 없으면 예외 처리
        } else {
            throw new RuntimeException("지원하지 않는 퀘스트 번호입니다.");
        }
    }

    public Quest findByCreatedAtAndUserId(LocalDate createdAt, Long userId) {
        return questRepository.findByCreatedAtAndUsers_UserId(createdAt, userId)
                .orElseThrow(() -> new RuntimeException("Not Found Quest"));
    }
}
