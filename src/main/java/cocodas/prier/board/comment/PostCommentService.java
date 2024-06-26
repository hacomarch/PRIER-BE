package cocodas.prier.board.comment;

import cocodas.prier.aws.AwsS3Service;
import cocodas.prier.board.comment.request.PostCommentRequestDto;
import cocodas.prier.board.comment.response.PostCommentListResponseDto;
import cocodas.prier.board.post.post.Post;
import cocodas.prier.board.post.post.PostRepository;
import cocodas.prier.user.UserRepository;
import cocodas.prier.user.Users;
import cocodas.prier.user.kakao.jwt.JwtTokenProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostCommentService {
    private final UserRepository userRepository;
    private final PostRepository postRepository;
    private final PostCommentRepository postCommentRepository;
    private final JwtTokenProvider jwtTokenProvider;
    private final AwsS3Service awsS3Service;

    @Transactional
    public void savePostComment(String token, Long postId, PostCommentRequestDto dto) {
        Users users = findUserByJwt(token);
        Post post = findPostById(postId);

        PostComment comment = PostComment.builder()
                .content(dto.getContent())
                .post(post)
                .users(users)
                .build();

        postCommentRepository.save(comment);
    }

    @Transactional
    public void updatePostComment(String token, Long commentId, PostCommentRequestDto dto) {
        Long userId = findUserIdByJwt(token);
        PostComment comment = findPostCommentById(commentId);
        if (!comment.getUsers().getUserId().equals(userId)) {
            throw new RuntimeException("No Authorized to update comment");
        }
        comment.updateContent(dto.getContent());
    }

    @Transactional
    public void deletePostComment(String token, Long commentId) {
        Long userId = findUserIdByJwt(token);
        PostComment comment = findPostCommentById(commentId);
        if (!comment.getUsers().getUserId().equals(userId)) {
            throw new RuntimeException("No Authorized to update comment");
        }
        postCommentRepository.delete(comment);
    }

    public List<PostCommentListResponseDto> findPostCommentByPostId(Long postId) {
        return postCommentRepository.findByPost_PostId(postId)
                .stream()
                .map(comment -> new PostCommentListResponseDto(
                        comment.getUsers().getUserId(),
                        awsS3Service.getPublicUrl(comment.getUsers().getS3Key()),
                        comment.getCommentId(),
                        comment.getContent(),
                        comment.getUsers().getNickname(),
                        comment.getCreatedAt(),
                        comment.getUpdatedAt()
                ))
                .toList();
    }

    private Users findUserByJwt(String token) {
        Long userId = findUserIdByJwt(token);
        return userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User Not Found"));
    }

    private Long findUserIdByJwt(String token) {
        return jwtTokenProvider.getUserIdFromJwt(token);
    }

    private Post findPostById(Long postId) {
        return postRepository.findById(postId)
                .orElseThrow(() -> new RuntimeException("Post Not Found"));
    }

    private PostComment findPostCommentById(Long commentId) {
        return postCommentRepository.findById(commentId)
                .orElseThrow(() -> new RuntimeException("Post Comment Not Found"));
    }
}
