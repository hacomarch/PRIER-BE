package cocodas.prier.user;

import cocodas.prier.user.dto.request.*;
import cocodas.prier.user.kakao.KakaoService;
import cocodas.prier.user.dto.response.LoginSuccessResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Slf4j
@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final KakaoService kakaoService;

    @Value("${kakao.get_code_path}")
    private String getCodePath;

    @Value("${kakao.client_id}")
    private String client_id;

    @Value(("${kakao.redirect_uri}"))
    private String redirect_uri;

    //TODO : 프론트에서 주소 보내기
    @GetMapping("/kakao/login")
    public String loginPage(Model model) {
        String location = getCodePath + client_id + "&redirect_uri=" + redirect_uri;
        model.addAttribute("location", location);

        return "login";
    }

    @GetMapping("/kakao/callback")
    public String callback(@RequestParam("code") String code, Model model) {
        LoginSuccessResponse userResponse = kakaoService.kakaoLogin(code);
        model.addAttribute("userResponse", userResponse);

        return "home";
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
}