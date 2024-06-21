package cocodas.prier.project.project.dto;

import cocodas.prier.project.project.ProjectStatus;
import cocodas.prier.project.tag.tag.dto.TagDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@AllArgsConstructor
public class ProjectDto {
    private Long projectId;
    private String title;
    private String teamName;
    private String mainImageUrl;
    private LocalDate devStartDate;
    private ProjectStatus status;
    private List<TagDto> tags;
    private Float score;
}
