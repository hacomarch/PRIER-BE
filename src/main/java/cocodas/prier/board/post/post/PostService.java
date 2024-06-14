package cocodas.prier.board.post.post;

import cocodas.prier.board.comment.PostCommentService;
import cocodas.prier.board.post.like.Likes;
import cocodas.prier.board.post.like.LikeRepository;
import cocodas.prier.board.post.post.request.PostRequestDto;
import cocodas.prier.board.post.post.response.PostDetailResponseDto;
import cocodas.prier.board.post.post.response.PostListResponseDto;
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
import java.util.stream.Stream;

@Slf4j
@Service
@AllArgsConstructor
public class PostService {
    private final PostRepository postRepository;

    private final LikeRepository likeRepository;

    private final JwtTokenProvider jwtTokenProvider;

    private UserRepository userRepository;

    private PostMediaService postMediaService;

    private PostCommentService postCommentService;

    // jwt 로 userId 찾기
    private Long findUserIdByJwt(String token) {
        return jwtTokenProvider.getUserIdFromJwt(token);
    }

    // userId 로 Users 객체 가져오기
    private Users findUserObject(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with id" + userId));
    }

    //

    // 게시글 조회하기
    public List<PostListResponseDto> allPostList() {
        return postRepository.findAll().stream()
                .map(post -> new PostListResponseDto(
                        post.getPostId(),
                        post.getTitle(),
                        post.getContent(),
                        post.getUsers().getNickname(),
                        post.getCategory().name(),
                        postMediaService.getPostMediaDetail(post),
                        post.getViews(),
                        post.getLikes().size(),
                        post.getCreatedAt(),
                        post.getUpdatedAt()
                ))
                .collect(Collectors.toList());
    }

    // postId로 게시글 조회하기(특정 글 조회하기)
    @Transactional
    public PostDetailResponseDto findByPostId(Long postId) {
        Post post = findById(postId);
        post.updateViews(post.getViews() + 1);

        return new PostDetailResponseDto(
                post.getUsers().getUserId(),
                post.getPostId(),
                post.getTitle(),
                post.getContent(),
                post.getUsers().getNickname(),
                post.getCategory().name(),
                postMediaService.getPostMediaDetail(post),
                post.getViews(),
                post.getLikes().size(),
                post.getCreatedAt(),
                post.getUpdatedAt(),
                postCommentService.findPostCommentByPostId(postId)
        );
    }

    // 검색어에 맞춰 게시글 조회하기
    public List<PostListResponseDto> searchPostsByKeyword(String keyword) {
        List<Post> postsByTitle = postRepository.findByTitleContaining(keyword);
        List<Post> postsByContent = postRepository.findByContentContaining(keyword);

        List<Post> combinedPosts = Stream.concat(postsByTitle.stream(), postsByContent.stream())
                .distinct()
                .toList();

        return combinedPosts.stream()
                .map(post -> new PostListResponseDto(
                        post.getPostId(),
                        post.getTitle(),
                        post.getContent(),
                        post.getUsers().getNickname(),
                        post.getCategory().name(),
                        postMediaService.getPostMediaDetail(post),
                        post.getViews(),
                        post.getLikes().size(),
                        post.getCreatedAt(),
                        post.getUpdatedAt()
                ))
                .collect(Collectors.toList());
    }

    // 내가 작성한 글 조회하기
    public List<PostListResponseDto> myPostList(String token) {
        Long userId = findUserIdByJwt(token);

        Users findUser = findUserObject(userId);

        List<Post> findUserPosts = postRepository.findByUsers(findUser);

        return findUserPosts.stream()
                .map(post -> new PostListResponseDto(
                        post.getPostId(),
                        post.getTitle(),
                        post.getContent(),
                        post.getUsers().getNickname(),
                        post.getCategory().name(),
                        postMediaService.getPostMediaDetail(post),
                        post.getViews(),
                        post.getLikes().size(),
                        post.getCreatedAt(),
                        post.getUpdatedAt()
                )).collect(Collectors.toList());
    }

    // 좋아요한 글 조회하기
    public List<PostListResponseDto> pushLikePost(String token) {
        Long userId = findUserIdByJwt(token);

        Users findUser = findUserObject(userId);

        // 사용자가 좋아요한 모든 Like 엔티티 조회
        List<Likes> likes = likeRepository.findByUsers(findUser);

        // Like 엔티티에서 게시글 ID 추출
        List<Long> postIds = likes.stream()
                .map(like -> like.getPost().getPostId())
                .collect(Collectors.toList());

        // 게시글 ID로 게시글 조회
        List<Post> posts = postRepository.findAllById(postIds);

        // Post 엔티티를 PostResponseDto 로 변환하여 반환
        return posts.stream()
                .map(post -> new PostListResponseDto(
                        post.getPostId(),
                        post.getTitle(),
                        post.getContent(),
                        post.getUsers().getNickname(),
                        post.getCategory().name(),
                        postMediaService.getPostMediaDetail(post),
                        post.getViews(),
                        post.getLikes().size(),
                        post.getCreatedAt(),
                        post.getUpdatedAt()
                )).collect(Collectors.toList());
    }

    // 게시글 작성하기
    @Transactional
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

        Post findPost = findById(boardId);

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

        Post findPost = findById(postId);

        if (!findPost.getUsers().getUserId().equals(userId)) {
            throw new IllegalStateException("해당 게시글을 삭제할 권한이 없습니다.");
        }

        postMediaService.deleteFile(findPost);
        postRepository.delete(findPost);
    }

    private Post findById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new IllegalArgumentException("해당 postId를 가진 Post가 없습니다."));
    }
}
