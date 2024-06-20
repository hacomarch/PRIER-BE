package cocodas.prier.project.feedback.response.dto;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseObjectiveDto {
    private Long questionId;
    private Integer veryGood;
    private Integer good;
    private Integer soso;
    private Integer bad;
    private Integer veryBad;
}
