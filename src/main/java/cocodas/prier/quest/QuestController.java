package cocodas.prier.quest;

import cocodas.prier.user.kakao.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class QuestController {
    private final QuestService questService;
    private final JwtTokenProvider jwtTokenProvider;

    @PutMapping("/quests/{date}/{sequence}")
    public ResponseEntity<String> quest(@PathVariable LocalDate date,
                                        @PathVariable int sequence,
                                        @RequestHeader("Authorization") String auth) {
        String token = getToken(auth);
        Long userId = jwtTokenProvider.getUserIdFromJwt(token);
        questService.updateQuest(date, userId, sequence);
        return ResponseEntity.ok("quest complete");
    }

    private static String getToken(String auth) {
        return auth.replace("Bearer ", "");
    }
}