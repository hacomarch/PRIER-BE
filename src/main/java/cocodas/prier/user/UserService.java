package cocodas.prier.user;

<<<<<<< HEAD
import cocodas.prier.user.kakao.jwt.JwtTokenProvider;
=======
>>>>>>> 704c3d42e2d9fbcfa39dd59e0ea1bee1c9066c0f
import cocodas.prier.user.kakao.jwt.token.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    private final RefreshTokenService refreshTokenService;

<<<<<<< HEAD
    private final JwtTokenProvider jwtTokenProvider;

    private Long findUserIdByJwt(String token) {
        return jwtTokenProvider.getUserIdFromJwt(token);
=======
    // refresh token 으로 사용자 식별하기
    private Long findUserByRefreshToken(String token) {
        return refreshTokenService.findUserIdByRefreshToken(token);
>>>>>>> 704c3d42e2d9fbcfa39dd59e0ea1bee1c9066c0f
    }

    // 닉네임 수정하기
    public void newNickName(String token, String newNickname) {
<<<<<<< HEAD
        Long userId = findUserIdByJwt(token);
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.updateNickName(newNickname);
        userRepository.save(user);
=======
        Long userId = findUserByRefreshToken(token);
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Users updatedUser = Users.builder()
                .nickname(newNickname)
                .intro(user.getIntro())
                .belonging(user.getBelonging())
                .tier(user.getTier())
                .blogUrl(user.getBlogUrl())
                .githubUrl(user.getGithubUrl())
                .figmaUrl(user.getFigmaUrl())
                .notionUrl(user.getNotionUrl())
                .lastLoginAt(user.getLastLoginAt())
                .build();

        userRepository.save(updatedUser);
>>>>>>> 704c3d42e2d9fbcfa39dd59e0ea1bee1c9066c0f
    }

    // 소속 수정하기
    public void newBelonging(String token, String newBelonging) {
<<<<<<< HEAD
        Long userId = findUserIdByJwt(token);
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.updateBelonging(newBelonging);
        userRepository.save(user);
=======
        Long userId = findUserByRefreshToken(token);
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Users updatedUser = Users.builder()
                .nickname(user.getNickname())
                .intro(user.getIntro())
                .belonging(newBelonging)
                .tier(user.getTier())
                .blogUrl(user.getBlogUrl())
                .githubUrl(user.getGithubUrl())
                .figmaUrl(user.getFigmaUrl())
                .notionUrl(user.getNotionUrl())
                .lastLoginAt(user.getLastLoginAt())
                .build();

        userRepository.save(updatedUser);
>>>>>>> 704c3d42e2d9fbcfa39dd59e0ea1bee1c9066c0f
    }

    // 자기소개 수정하기
    public void newIntro(String token, String newIntro) {
<<<<<<< HEAD
        Long userId = findUserIdByJwt(token);
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.updateIntro(newIntro);
        userRepository.save(user);
=======
        Long userId = findUserByRefreshToken(token);
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Users updatedUser = Users.builder()
                .nickname(user.getNickname())
                .intro(newIntro)
                .belonging(user.getBelonging())
                .tier(user.getTier())
                .blogUrl(user.getBlogUrl())
                .githubUrl(user.getGithubUrl())
                .figmaUrl(user.getFigmaUrl())
                .notionUrl(user.getNotionUrl())
                .lastLoginAt(user.getLastLoginAt())
                .build();

        userRepository.save(updatedUser);
>>>>>>> 704c3d42e2d9fbcfa39dd59e0ea1bee1c9066c0f
    }

    // 블로그 링크 수정하기
    public void newBlogUrl(String token, String newBlogUrl) {
<<<<<<< HEAD
        Long userId = findUserIdByJwt(token);
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.updateBlog(newBlogUrl);
        userRepository.save(user);
=======
        Long userId = findUserByRefreshToken(token);
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Users updatedUser = Users.builder()
                .nickname(user.getNickname())
                .intro(user.getIntro())
                .belonging(user.getBelonging())
                .tier(user.getTier())
                .blogUrl(newBlogUrl)
                .githubUrl(user.getGithubUrl())
                .figmaUrl(user.getFigmaUrl())
                .notionUrl(user.getNotionUrl())
                .lastLoginAt(user.getLastLoginAt())
                .build();

        userRepository.save(updatedUser);
>>>>>>> 704c3d42e2d9fbcfa39dd59e0ea1bee1c9066c0f
    }

    // 깃헙 링크 수정하기
    public void newGithubUrl(String token, String newGithubUrl) {
<<<<<<< HEAD
        Long userId = findUserIdByJwt(token);
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.updateGithub(newGithubUrl);
        userRepository.save(user);
=======
        Long userId = findUserByRefreshToken(token);
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Users updatedUser = Users.builder()
                .nickname(user.getNickname())
                .intro(user.getIntro())
                .belonging(user.getBelonging())
                .tier(user.getTier())
                .blogUrl(user.getBlogUrl())
                .githubUrl(newGithubUrl)
                .figmaUrl(user.getFigmaUrl())
                .notionUrl(user.getNotionUrl())
                .lastLoginAt(user.getLastLoginAt())
                .build();

        userRepository.save(updatedUser);
>>>>>>> 704c3d42e2d9fbcfa39dd59e0ea1bee1c9066c0f
    }

    // 피그마 주소 수정하기
    public void newFigmaUrl(String token, String newFigmaUrl) {
<<<<<<< HEAD
        Long userId = findUserIdByJwt(token);
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.updateFigma(newFigmaUrl);
        userRepository.save(user);
=======
        Long userId = findUserByRefreshToken(token);
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Users updatedUser = Users.builder()
                .nickname(user.getNickname())
                .intro(user.getIntro())
                .belonging(user.getBelonging())
                .tier(user.getTier())
                .blogUrl(user.getBlogUrl())
                .githubUrl(user.getGithubUrl())
                .figmaUrl(newFigmaUrl)
                .notionUrl(user.getNotionUrl())
                .lastLoginAt(user.getLastLoginAt())
                .build();

        userRepository.save(updatedUser);
>>>>>>> 704c3d42e2d9fbcfa39dd59e0ea1bee1c9066c0f
    }

    // 노션 주소 수정하기
    public void newNotionUrl(String token, String newNotionUrl) {
<<<<<<< HEAD
        Long userId = findUserIdByJwt(token);
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.updateNotion(newNotionUrl);
        userRepository.save(user);
=======
        Long userId = findUserByRefreshToken(token);
        Users user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Users updatedUser = Users.builder()
                .nickname(user.getNickname())
                .intro(user.getIntro())
                .belonging(user.getBelonging())
                .tier(user.getTier())
                .blogUrl(user.getBlogUrl())
                .githubUrl(user.getGithubUrl())
                .figmaUrl(user.getFigmaUrl())
                .notionUrl(newNotionUrl)
                .lastLoginAt(user.getLastLoginAt())
                .build();

        userRepository.save(updatedUser);
>>>>>>> 704c3d42e2d9fbcfa39dd59e0ea1bee1c9066c0f
    }

    // 퀘스트하기

}
