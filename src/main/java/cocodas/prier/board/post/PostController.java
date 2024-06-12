package cocodas.prier.board.post;

import cocodas.prier.board.post.post.request.PostRequestDto;
import cocodas.prier.board.post.post.PostService;
import cocodas.prier.board.post.post.response.PostResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    // 모든 게시글 조회하기 & 검색 키워드 게시글 조회하기
    @GetMapping("/boards")
    @ResponseStatus(HttpStatus.OK)
    public List<PostResponseDto> allOrSearchPosts(@RequestParam(name = "search", required = false) String keyword) {
        if (keyword == null || keyword.isEmpty()) {
            return postService.allPostList();
        } else {
            return postService.searchPostsByKeyword(keyword);
        }
    }

    // 카테고리에 맞춰 조회하기
    @GetMapping("/boards/{category}")
    @ResponseStatus(HttpStatus.OK)
    public List<PostResponseDto> categorySearch(@PathVariable(name = "category") String category) {
        return postService.categorySearch(category);
    }

    // 내가 작성한 게시글 모두 조회하기
    @GetMapping("/boards/my")
    @ResponseStatus(HttpStatus.OK)
    public List<PostResponseDto> myPostList(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        return postService.myPostList(token);
    }

    // 내가 좋아요 누른 게시글 모두 조회하기
    @GetMapping("/boards/like/my")
    @ResponseStatus(HttpStatus.OK)
    public List<PostResponseDto> pushLikePost(@RequestHeader("Authorization") String authorizationHeader) {
        String token = authorizationHeader.replace("Bearer ", "");
        return postService.pushLikePost(token);
    }

    // 게시글 작성하기
    @PostMapping("/boards")
    @ResponseStatus(HttpStatus.CREATED)
    public void addPost(@RequestHeader("Authorization") String authorizationHeader,
                        @RequestPart("dto") PostRequestDto postRequestDto,
                        @RequestParam(name = "media", required = false) MultipartFile[] media) {
        String token = authorizationHeader.replace("Bearer ", "");
        postService.addPost(token, postRequestDto, media);
    }

    // 게시글 수정하기
    @PutMapping("/boards/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public void updatePost(@RequestHeader("Authorization") String authorizationHeader,
                           @RequestPart("dto") PostRequestDto postRequestDto,
                           @RequestParam(name = "media", required = false) MultipartFile[] media,
                           @PathVariable(name = "postId") Long postId) {
        String token = authorizationHeader.replace("Bearer ", "");
        postService.updatePost(token, postRequestDto, postId, media);
    }

    // 게시글 삭제하기
    @DeleteMapping("/boards/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public void deletePost(@RequestHeader("Authorization") String authorizationHeader,
                           @PathVariable(name = "postId") Long postId) {
        String token = authorizationHeader.replace("Bearer ", "");
        postService.deletePost(token, postId);
    }
}
