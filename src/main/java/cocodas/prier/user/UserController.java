package cocodas.prier.user;

import cocodas.prier.user.kakao.KakaoService;
import cocodas.prier.user.dto.response.LoginSuccessResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
}
