package cocodas.prier.project.project.dto;

import cocodas.prier.project.feedback.question.dto.QuestionDto;
import cocodas.prier.project.media.dto.ProjectMediaDto;
import cocodas.prier.project.project.ProjectStatus;
import cocodas.prier.project.tag.tag.dto.TagDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Data
@AllArgsConstructor
public class ProjectDetailDto {
    private Long projectId;
    private String title;
    private String introduce;
    private String goal;
    private LocalDate startDate;
    private LocalDate endDate;
    private ProjectStatus status;
    private String teamName;
    private String teamDescription;
    private String teamMate;
    private String link;
    private Boolean isMine;
    private LocalDateTime feedbackEndDate;
    private List<QuestionDto> questions;
    private List<ProjectMediaDto> media;
    private List<TagDto> tags;
    private Float score;
    private int[] amount;
    private String profileImageUrl;
}

