package cocodas.prier.quest;

import cocodas.prier.project.comment.ProjectCommentRepository;
import cocodas.prier.project.feedback.response.ResponseRepository;
import cocodas.prier.user.Users;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class QuestService {
    private final QuestRepository questRepository;
    private final ResponseRepository responseRepository;
    private final ProjectCommentRepository projectCommentRepository;

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
    public String updateQuest(LocalDate createdAt, Long userId, int sequence) {
        try {
            Quest quest = findByCreatedAtAndUserId(createdAt, userId);
            switch (sequence) {
                case 1:
                    quest.updateFirst(true);
                    break;
                case 2:
                    validateAndUpdateSecondQuest(userId, quest);
                    break;
                case 3:
                    validateAndUpdateThirdQuest(userId, quest);
                    break;
                default:
                    return "지원하지 않는 퀘스트 번호입니다.";
            }
            return "퀘스트가 성공적으로 업데이트되었습니다.";
        } catch (RuntimeException e) {
            return e.getMessage();
        }
    }

    public Quest findByCreatedAtAndUserId(LocalDate createdAt, Long userId) {
        return questRepository.findByCreatedAtAndUsers_UserId(createdAt, userId)
                .orElseThrow(() -> new RuntimeException("퀘스트를 찾을 수 없습니다."));
    }

    private void validateAndUpdateSecondQuest(Long userId, Quest quest) {
        if (quest.getFirst() && isHasTodayComment(userId)) {
            quest.updateSecond(true);
        } else {
            throw new RuntimeException("오늘 작성한 댓글이 없거나 첫 번째 퀘스트가 완료되지 않았습니다.");
        }
    }

    private boolean isHasTodayComment(Long userId) {
        return projectCommentRepository.findByUsers_UserId(userId)
                .stream()
                .anyMatch(comment -> isToday(comment.getCreatedAt()));
    }

    private void validateAndUpdateThirdQuest(Long userId, Quest quest) {
        if (quest.getSecond() && isHasTodayResponse(userId)) {
            quest.updateThird(true);
        } else {
            throw new RuntimeException("오늘 작성한 피드백이 없거나 두 번째 퀘스트가 완료되지 않았습니다.");
        }
    }

    private boolean isHasTodayResponse(Long userId) {
        return responseRepository.findAllByUsers_UserId(userId)
                .stream()
                .anyMatch(comment -> isToday(comment.getCreatedAt()));
    }

    private boolean isToday(LocalDateTime dateTime) {
        return dateTime.toLocalDate().equals(LocalDate.now());
    }
}