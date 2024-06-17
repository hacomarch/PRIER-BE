package cocodas.prier.statics.keywordAi.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class KeyWordResponseDto {
    private Long projectId;
    private String content;
    private int count;
}
