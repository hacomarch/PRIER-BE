package cocodas.prier.statics.keywordAi;

import cocodas.prier.statics.keywordAi.dto.response.KeyWordResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class KeywordsController {
    private final KeywordsService keywordsService;

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/api/{projectId}/statics/keywords")
    public List<KeyWordResponseDto> getKeywords(@RequestHeader("Authorization") String authorizationHeader,
                                                @PathVariable Long projectId) {
        String token = authorizationHeader.replace("Bearer ", "");
        return keywordsService.getKeywordByProjectId(projectId, token);
    }

}
