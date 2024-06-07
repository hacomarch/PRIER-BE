package cocodas.prier.user;

import cocodas.prier.user.kakao.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final JwtTokenProvider jwtTokenProvider;

    private Long findUserIdByJwt(String token) {
        return jwtTokenProvider.getUserIdFromJwt(token);
    }

    // 닉네임 수정하기
    @Transactional
    public void newNickName(String token, String newNickname) {
        Long userId = findUserIdByJwt(token);
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.updateNickName(newNickname);
    }

    // 소속 수정하기
    @Transactional
    public void newBelonging(String token, String newBelonging) {
        Long userId = findUserIdByJwt(token);
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.updateBelonging(newBelonging);
    }

    // 자기소개 수정하기
    @Transactional
    public void newIntro(String token, String newIntro) {
        Long userId = findUserIdByJwt(token);
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.updateIntro(newIntro);
    }

    // 블로그 링크 수정하기
    @Transactional
    public void newBlogUrl(String token, String newBlogUrl) {
        Long userId = findUserIdByJwt(token);
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.updateBlog(newBlogUrl);
    }

    // 깃헙 링크 수정하기
    @Transactional
    public void newGithubUrl(String token, String newGithubUrl) {
        Long userId = findUserIdByJwt(token);
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.updateGithub(newGithubUrl);
    }

    // 피그마 주소 수정하기
    @Transactional
    public void newFigmaUrl(String token, String newFigmaUrl) {
        Long userId = findUserIdByJwt(token);
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.updateFigma(newFigmaUrl);
    }

    // 노션 주소 수정하기
    @Transactional
    public void newNotionUrl(String token, String newNotionUrl) {
        Long userId = findUserIdByJwt(token);
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.updateNotion(newNotionUrl);
    }
}