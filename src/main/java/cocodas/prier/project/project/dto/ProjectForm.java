package cocodas.prier.project.project.dto;

import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class ProjectForm {
    private String title;
    private String introduce;
    private String goal;
    //태그 없을수도 있으니 기본값 설정
    private String[] tags = null;
    private LocalDate startDate;
    private LocalDate endDate;
    //0, 1, 2로 구분해서 받기 (Enum)
    private int status;
    private String teamName;
    private String teamDescription;
    private String teamMate;
    private String link;
    private String[] question;
    private Long[] questionId = null;
    private String[] type;
}