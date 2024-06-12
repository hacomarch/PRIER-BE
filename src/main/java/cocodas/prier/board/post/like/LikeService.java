package cocodas.prier.board.post.like;

import cocodas.prier.board.post.like.request.LikeRequestDto;
import cocodas.prier.board.post.post.Post;
import cocodas.prier.board.post.post.PostRepository;
import cocodas.prier.user.UserRepository;
import cocodas.prier.user.Users;
import cocodas.prier.user.kakao.jwt.JwtTokenProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@AllArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;

    private final JwtTokenProvider jwtTokenProvider;

    private UserRepository userRepository;

    private PostRepository postRepository;

    // jwt 로 userId 찾기
    private Long findUserIdByJwt(String token) {
        return jwtTokenProvider.getUserIdFromJwt(token);
    }

    // userId 로 Users 객체 가져오기
    private Users findUserObject(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id" + userId));
    }

    // postId 로 Post 객체 가져오기
    private Post findPostObject(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("Post not found with id" + postId));
    }

    // 좋아요 누르기
    @Transactional
    public void pushLike(String token, LikeRequestDto likeRequestDto) {
        Long userId = findUserIdByJwt(token);

        Users findUser = findUserObject(userId);

        Post findPost = findPostObject(likeRequestDto.getPostId());

        Likes like = Likes.builder()
                .users(findUser)
                .post(findPost)
                .build();

        likeRepository.save(like);
    }
}
