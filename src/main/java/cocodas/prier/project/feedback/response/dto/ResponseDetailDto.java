package cocodas.prier.project.feedback.response.dto;

import cocodas.prier.project.comment.dto.CommentWithProfileDto;
import cocodas.prier.statics.chatgpt.response.SummaryResponse;
import cocodas.prier.statics.keywordAi.dto.response.KeyWordResponseDto;
import cocodas.prier.user.response.ProfileImgDto;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class ResponseDetailDto {
    private Long projectId;
    private String title;
    private String introduce;
    private String teamName;
    private String link;
    private String projectImage;
    private Float averageScore;
    private List<KeyWordResponseDto> keyWordResponseDtoList;
    private Integer feedbackCount;
    private String percentage;
    private List<SummaryResponse> chatGpt;                          // 주관식
    private List<ResponseObjectiveDto> responseObjectiveDtoList;    // 객관식
    private List<CommentWithProfileDto> commentWithProfileDtoList;
    private ProfileImgDto myProfileImgDto;
}
