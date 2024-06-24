package cocodas.prier.user.response;

import cocodas.prier.project.comment.dto.MyPageCommentDto;
import cocodas.prier.statics.keywordAi.dto.response.KeyWordResponseDto;
import cocodas.prier.user.Rank;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
public class MyPageResponseDto {
    private String nickname;
    private String belonging;
    private Rank rank;
    private String githubUrl;
    private String notionUrl;
    private String blogUrl;
    private String figmaUrl;
    private String email;
    private String intro;
    private Boolean firstQuest;
    private Boolean secondQuest;
    private Boolean thirdQuest;
    private Long nowProjectId;
    private String nowProjectTeamName;
    private String nowProjectName;
    private Integer nowProjectFeedbackCount;
    private Float nowProjectScore;
    private String nowProjectStaticPercentage;
    private List<KeyWordResponseDto> nowProjectKeywordList;
    private List<MyPageCommentDto> myPageCommentDtoList;
    private ProfileImgDto profileImgDto;
}
