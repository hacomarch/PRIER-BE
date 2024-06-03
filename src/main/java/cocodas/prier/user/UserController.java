package cocodas.prier.user;

import cocodas.prier.user.dto.response.KakaoUserInfoResponseDto;
import cocodas.prier.user.kakao.jwt.JwtTokenProvider;
import cocodas.prier.user.kakao.KakaoService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final KakaoService kakaoService;
    private final JwtTokenProvider jwtTokenProvider;

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
        String accessToken = kakaoService.getAccessToken(code);
        KakaoUserInfoResponseDto userInfo = kakaoService.getUserInfo(accessToken);
        Long userId = kakaoService.getUserByEmail(userInfo.getKakaoAccount().email)
                .getUserId();
        Authentication authentication = new UsernamePasswordAuthenticationToken(userId, null, null);
        String jwtToken = jwtTokenProvider.generateToken(authentication);


        model.addAttribute("userInfo", userInfo);
        model.addAttribute("jwtToken", jwtToken);
        return "home";
    }
}
