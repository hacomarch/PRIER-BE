package cocodas.prier.project.media.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProjectMediaDto {
    private Long id;
    private String metadata;
    private boolean isMain;
    private String mediaType;
    private String url;
    private int orderIndex;
}
