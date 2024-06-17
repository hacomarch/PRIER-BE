package cocodas.prier.project.project.dto;

import cocodas.prier.project.media.dto.ProjectMediaDto;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProjectDetailForFeedbackPageDto {

    private Long projectId;
    private String title;
    private String teamName;
    private String introduce;
    private ProjectMediaDto media;
    private String goal;
    private String link;
    private int[] amount;
}
