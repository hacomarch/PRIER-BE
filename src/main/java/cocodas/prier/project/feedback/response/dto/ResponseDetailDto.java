package cocodas.prier.project.feedback.response.dto;


/*
   프로젝트(프로젝트 이름, 팀 이름, 프로젝트 상세내용, 깃허브 링크 or 배포링크)
   AI 키워드 분석
   제출된 피드백 개수
   객관식 통계
   상세응답 분석
       - 주관식은 chatGPT 요약 2줄
       - 객관식은 10 ~ 50까지 개수 count
   해당 프로젝트의 댓글(내용, 작성자, 별점, 소속)
   * */

import cocodas.prier.project.comment.dto.MyPageCommentDto;
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
    private List<KeyWordResponseDto> keyWordResponseDtoList;
    private Integer feedbackCount;
    private String percentage;
    private List<SummaryResponse> chatGpt;
    private List<ResponseObjectiveDto> responseObjectiveDtoList;
    private List<MyPageCommentDto> myPageCommentDtoList;
    private ProfileImgDto profileImgDto;

}
