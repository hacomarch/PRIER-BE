package cocodas.prier.user;

import cocodas.prier.user.dto.request.*;
import cocodas.prier.user.response.MyPageResponseDto;
import cocodas.prier.user.response.OhterMyPageResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
@CrossOrigin(origins = "http://localhost:3000")
public class UserController {
    private final UserService userService;

    // 나의 마이페이지 보기
    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/mypage")
    public MyPageResponseDto viewMyPage(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        return userService.viewMyPage(token);
    }

    // 다른 사람의 마이페이지 보기
    @GetMapping("/mypage/{userId}")
    public OhterMyPageResponseDto viewOtherPage(@PathVariable(name = "userId") Long userId,
                                                @RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        return userService.viewOtherMyPage(token, userId);
    }

    // 마이페이지 수정하기 Controller
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/users/nickname")
    public void newNickName(@RequestHeader("Authorization") String authorizationHeader,
                            @RequestBody NickNameRequestDto nickNameRequestDto) {
        String token = authorizationHeader.replace("Bearer ", "");
        userService.newNickName(token, nickNameRequestDto.getNickname());
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/users/belonging")
    public void newBelonging(@RequestHeader("Authorization") String authorizationHeader,
                             @RequestBody BelongingRequestDto belongingRequestDto) {
        String token = authorizationHeader.replace("Bearer ", "");
        userService.newBelonging(token, belongingRequestDto.getBelonging());
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/users/intro")
    public void newIntro(@RequestHeader("Authorization") String authorizationHeader,
                         @RequestBody IntroRequestDto introRequestDto) {
        String token = authorizationHeader.replace("Bearer ", "");
        userService.newIntro(token, introRequestDto.getIntro());
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/users/blog")
    public void newBlogUrl(@RequestHeader("Authorization") String authorizationHeader,
                         @RequestBody BlogUrlRequestDto blogUrlRequestDto) {
        String token = authorizationHeader.replace("Bearer ", "");
        userService.newBlogUrl(token, blogUrlRequestDto.getBlogUrl());
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/users/github")
    public void newGithubUrl(@RequestHeader("Authorization") String authorizationHeader,
                         @RequestBody GithubUrlRequestDto githubUrlRequestDto) {
        String token = authorizationHeader.replace("Bearer ", "");
        userService.newGithubUrl(token, githubUrlRequestDto.getGithubUrl());
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/users/figma")
    public void newFigmaUrl(@RequestHeader("Authorization") String authorizationHeader,
                         @RequestBody FigmaUrlRequestDto figmaUrlRequestDto) {
        String token = authorizationHeader.replace("Bearer ", "");
        userService.newFigmaUrl(token, figmaUrlRequestDto.getFigmaUrl());
    }

    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/users/notion")
    public void newNotionUrl(@RequestHeader("Authorization") String authorizationHeader,
                         @RequestBody NotionUrlRequestDto notionUrlRequestDto) {
        String token = authorizationHeader.replace("Bearer ", "");
        userService.newNotionUrl(token, notionUrlRequestDto.getNotionUrl());
    }

    // 프로필 사진 수정하기
    @ResponseStatus(HttpStatus.OK)
    @PutMapping("/users/profile/img")
    public void newProfileImg(@RequestHeader("Authorization") String authorizationHeader,
                              @RequestParam(name = "media") MultipartFile media) throws IOException {
        String token = authorizationHeader.replace("Bearer ", "");
        userService.newProfileImg(token, media);
    }

    // 프로필 사진 삭제하기
    @ResponseStatus(HttpStatus.OK)
    @DeleteMapping("/users/profile/img")
    public void deleteProfileImg(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        userService.deleteProfileImg(token);
    }

}