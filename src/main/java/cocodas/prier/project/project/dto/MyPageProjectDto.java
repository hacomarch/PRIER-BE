package cocodas.prier.project.project.dto;

import cocodas.prier.project.tag.tag.dto.TagDto;
import lombok.Builder;
import lombok.Data;

import java.util.List;

//최신 프로젝트
@Data
@Builder
public class MyPageProjectDto {
    private Long projectId;
    private String title;
    private String teamName;
    private Float score;
    private Integer feedbackAmount;
}
