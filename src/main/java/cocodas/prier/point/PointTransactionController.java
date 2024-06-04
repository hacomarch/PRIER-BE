package cocodas.prier.point;


import cocodas.prier.point.dto.PointRechargeRequest;
import cocodas.prier.point.dto.PointTransactionDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/points")
public class PointTransactionController {

    @Autowired
    private PointTransactionService pointTransactionService;

    @GetMapping
    public Integer getCurrentPoints(@RequestParam Long userId) {
        return pointTransactionService.getCurrentPoints(userId);
    }

    @GetMapping("/history")
    public List<PointTransactionDTO> getPointHistory(@RequestParam Long userId) {
        return pointTransactionService.getPointHistory(userId);
    }

    @PostMapping("/recharge")
    public PointTransactionDTO rechargePoints(@RequestBody PointRechargeRequest request) {
        return pointTransactionService.rechargePoints(request);
    }

}
