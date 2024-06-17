package cocodas.prier.point;

import cocodas.prier.point.dto.PointTransactionDTO;
import cocodas.prier.user.kakao.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/points")
public class PointTransactionController {

    private final PointTransactionService pointTransactionService;
    private final JwtTokenProvider jwtTokenProvider;

    private static String getToken(String auth) {
        if (auth == null || !auth.startsWith("Bearer ")) {
            throw new RuntimeException("JWT Token is missing");
        }
        return auth.substring(7);
    }

    @GetMapping
    public Integer getCurrentPoints(@RequestHeader("Authorization") String auth) {
        String token = getToken(auth);
        Long userId = jwtTokenProvider.getUserIdFromJwt(token);
        return pointTransactionService.getCurrentPoints(userId);
    }

    @GetMapping("/history")
    public List<PointTransactionDTO> getPointHistory(@RequestHeader("Authorization") String auth) {
        String token = getToken(auth);
        Long userId = jwtTokenProvider.getUserIdFromJwt(token);
        return pointTransactionService.getPointHistory(userId);
    }

}