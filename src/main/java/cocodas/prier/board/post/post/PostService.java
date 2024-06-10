package cocodas.prier.board.post.post;

import cocodas.prier.board.post.post.request.PostRequestDto;
import cocodas.prier.user.UserRepository;
import cocodas.prier.user.Users;
import cocodas.prier.user.kakao.jwt.JwtTokenProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;

@Slf4j
@Service
@AllArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    private final JwtTokenProvider jwtTokenProvider;

    private UserRepository userRepository;

    // jwt 로 userId 찾기
    private Long findUserIdByJwt(String token) {
        return jwtTokenProvider.getUserIdFromJwt(token);
    }

    // userId 로 Users 객체 가져오기
    private Users findUserObject(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id" + userId));
    }

    // TODO : 게시글 조회하기

    // TODO : 검색어에 맞춰 게시글 조회하기

    // TODO : 카테고리에 맞춰 게시글 조회하기

    // TODO : 내가 작성한 글 조회하기 -> user에서 해야 하나?

    // TODO : 좋아요한 글 조회하기 -> user에서 해야 하나?


    @Transactional
    // 게시글 작성하기
    public void addPost(String token, PostRequestDto postRequestDto) {
        Long userId = findUserIdByJwt(token);

        Users findUser = findUserObject(userId);

        Post post = Post.builder()
                .users(findUser)
                .title(postRequestDto.getTitle())
                .category(postRequestDto.getCategory())
                .content(postRequestDto.getContent())
                .createdAt(LocalDateTime.now())
                .build();

        postRepository.save(post);
    }

    // TODO: 코드 수정할 수 있으면 하기...ㅎㅎ (if 문이 너무 거슬려요...ㅎㅎ)
    // 게시글 수정하기
    @Transactional
    public void updatePost(String token, PostRequestDto postRequestDto, Long boardId) {
        Long userId = findUserIdByJwt(token);

        Post findPost = postRepository.findById(boardId)
                .orElseThrow(() -> new IllegalArgumentException("해당 postId를 가진 Post가 없습니다."));

        if (!findPost.getUsers().getUserId().equals(userId)) {
            throw new IllegalStateException("해당 게시글을 수정할 권한이 없습니다.");
        }

        findPost.updateTitle(postRequestDto.getTitle());        // 제목 수정하기
        findPost.updateCategory(postRequestDto.getCategory());  // 카테고리 수정하기
        findPost.updateContent(postRequestDto.getContent());    // 내용 수정하기
        findPost.updateUpdatedAt(LocalDateTime.now());          // 수정한 시간 수정하기
    }

    // 게시글 삭제하기
    @Transactional
    public void deletePost(String token, Long postId) {
        Long userId = findUserIdByJwt(token);

        Post findPost = postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 postId를 가진 Post가 없습니다."));

        if (!findPost.getUsers().getUserId().equals(userId)) {
            throw new IllegalStateException("해당 게시글을 삭제할 권한이 없습니다.");
        }

        postRepository.delete(findPost);
    }
}
