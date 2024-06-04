package cocodas.prier.user;

import cocodas.prier.user.kakao.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

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
    public void newNickName(String token, String newNickname) {
        Long userId = findUserIdByJwt(token);
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.updateNickName(newNickname);
        userRepository.save(user);
    }

    // 소속 수정하기
    public void newBelonging(String token, String newBelonging) {
        Long userId = findUserIdByJwt(token);
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.updateBelonging(newBelonging);
        userRepository.save(user);
    }

    // 자기소개 수정하기
    public void newIntro(String token, String newIntro) {
        Long userId = findUserIdByJwt(token);
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.updateIntro(newIntro);
        userRepository.save(user);
    }

    // 블로그 링크 수정하기
    public void newBlogUrl(String token, String newBlogUrl) {
        Long userId = findUserIdByJwt(token);
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.updateBlog(newBlogUrl);
        userRepository.save(user);
    }

    // 깃헙 링크 수정하기
    public void newGithubUrl(String token, String newGithubUrl) {
        Long userId = findUserIdByJwt(token);
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.updateGithub(newGithubUrl);
        userRepository.save(user);
    }

    // 피그마 주소 수정하기
    public void newFigmaUrl(String token, String newFigmaUrl) {
        Long userId = findUserIdByJwt(token);
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.updateFigma(newFigmaUrl);
        userRepository.save(user);
    }

    // 노션 주소 수정하기
    public void newNotionUrl(String token, String newNotionUrl) {
        Long userId = findUserIdByJwt(token);
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.updateNotion(newNotionUrl);
        userRepository.save(user);
    }

    // 퀘스트하기

}
