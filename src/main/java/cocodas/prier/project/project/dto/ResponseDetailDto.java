package cocodas.prier.project.project.dto;

import cocodas.prier.project.tag.tag.dto.TagDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ResponseDetailDto {
    private Long projectId;
    private String title;
    private String teamName;
    private String mainImageUrl;
    private String introduce;
    private String link;
}
