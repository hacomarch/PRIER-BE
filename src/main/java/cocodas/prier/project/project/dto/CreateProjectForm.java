package cocodas.prier.project.project.dto;

import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
public class CreateProjectForm {
    private String title;
    private String introduce;
    private String goal;
    private String teamName;
    private LocalDateTime startDate;
    private LocalDateTime endDate;
    private MultipartFile[] files;
}
