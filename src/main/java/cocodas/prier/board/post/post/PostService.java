package cocodas.prier.board.post.post;

import cocodas.prier.board.post.post.request.PostRequestDto;
import cocodas.prier.board.post.post.response.PostResponseDto;
import cocodas.prier.board.post.postmedia.PostMediaService;
import cocodas.prier.user.UserRepository;
import cocodas.prier.user.Users;
import cocodas.prier.user.kakao.jwt.JwtTokenProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@AllArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    private final JwtTokenProvider jwtTokenProvider;

    private UserRepository userRepository;

    private PostMediaService postMediaService;

    // jwt 로 userId 찾기
    private Long findUserIdByJwt(String token) {
        return jwtTokenProvider.getUserIdFromJwt(token);
    }

    // userId 로 Users 객체 가져오기
    private Users findUserObject(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id" + userId));
    }

    // 게시글 조회하기
    public List<PostResponseDto> allPostList() {
        return postRepository.findAll().stream()
                .map(post -> PostResponseDto.builder()
                        .boardId(post.getPostId())
                        .title(post.getTitle())
                        .createdAt(post.getCreatedAt())
                        .updatedAt(post.getUpdatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    // TODO : 검색어에 맞춰 게시글 조회하기
//    public List<PostResponseDto> searchPosts(String keyword) {
//        List<Post> postsByTitle = postRepository.findByTitleContaining(keyword);
//        List<Post> postsByContent = postRepository.findByContentContaining(keyword);
//
//        // 두 리스트를 합쳐 중복을 제거합니다.
//        List<Post> combinedPosts = Stream.concat(postsByTitle.stream(), postsByContent.stream())
//                .distinct()
//                .collect(Collectors.toList());
//
//        return combinedPosts.stream()
//                .map(post -> PostResponseDto.builder()
//                        .boardId(post.getPostId())
//                        .title(post.getTitle())
//                        .createdAt(post.getCreatedAt())
//                        .updatedAt(post.getUpdatedAt())
//                        .build())
//                .collect(Collectors.toList());
//    }

    // 카테고리에 맞춰 게시글 조회하기
    public List<PostResponseDto> categorySearch(String category) {
        Category categoryEnum = Category.valueOf(category.toUpperCase());
        List<Post> posts = postRepository.findByCategory(categoryEnum);
        return posts.stream()
                .map(post -> PostResponseDto.builder()
                        .boardId(post.getPostId())
                        .title(post.getTitle())
                        .createdAt(post.getCreatedAt())
                        .updatedAt(post.getUpdatedAt())
                        .build())
                .collect(Collectors.toList());
    }

    // 내가 작성한 글 조회하기 -> user에서 해야 하나?
    public List<PostResponseDto> myPostList(String token) {
        Long userId = findUserIdByJwt(token);

        Users findUser = findUserObject(userId);

        List<Post> findUserPosts = postRepository.findByUsers(findUser);

        return findUserPosts.stream()
                .map(post -> PostResponseDto.builder()
                        .boardId(post.getPostId())
                        .title(post.getTitle())
                        .createdAt(post.getCreatedAt())
                        .updatedAt(post.getUpdatedAt())
                        .build())
                .collect(Collectors.toList());
    }


    // TODO : 좋아요한 글 조회하기 -> user에서 해야 하나?


    @Transactional
    // 게시글 작성하기
    public void addPost(String token, PostRequestDto postRequestDto, MultipartFile[] files) {
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
        uploadMedia(files, post);
    }

    private void uploadMedia(MultipartFile[] files, Post post) {
        try {
            postMediaService.uploadFile(post, files);
        } catch (IOException e) {
            throw new RuntimeException("Failed Upload Media");
        }
    }

    // 게시글 수정하기
    @Transactional
    public void updatePost(String token, PostRequestDto postRequestDto, Long boardId, MultipartFile[] media) {
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
        updateMedia(media, findPost);
    }

    private void updateMedia(MultipartFile[] media, Post findPost) {
        try {
            postMediaService.updateFile(findPost, media);
        } catch (IOException e) {
            throw new RuntimeException("Failed Update Media");
        }
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

        postMediaService.deleteFile(findPost);
        postRepository.delete(findPost);
    }
}
