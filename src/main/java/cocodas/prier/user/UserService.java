package cocodas.prier.user;

import cocodas.prier.user.kakao.jwt.token.RefreshTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RequestBody;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;

    private final RefreshTokenService refreshTokenService;

    // refresh token 으로 사용자 식별하기
    private Long findUserByRefreshToken(String token) {
        return refreshTokenService.findIdByRefreshToken(token);
    }

    // 닉네임 수정하기
    public void newNickName(String token, String newNickname) {
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
    }

    // 소속 수정하기
    public void newBelonging(String token, String newBelonging) {
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
    }

    // 자기소개 수정하기
    public void newIntro(String token, String newIntro) {
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
    }

    // 블로그 링크 수정하기
    public void newBlogUrl(String token, String newBlogUrl) {
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
    }

    // 깃헙 링크 수정하기
    public void newGithubUrl(String token, String newGithubUrl) {
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
    }

    // 피그마 주소 수정하기
    public void newFigmaUrl(String token, String newFigmaUrl) {
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
    }

    // 노션 주소 수정하기
    public void newNotionUrl(String token, String newNotionUrl) {
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
    }

    // 퀘스트하기

}
