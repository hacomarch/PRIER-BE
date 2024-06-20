package cocodas.prier.statics.chatgpt;

import cocodas.prier.statics.chatgpt.response.SummaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatGPTController {
    private final ChatGPTService chatGPTService;

    @GetMapping("/api/chatgpt/{projectId}")
    public List<SummaryResponse> getSummarize(@PathVariable Long projectId) {
        return chatGPTService.getChatGptResponse(projectId);
    }
}
