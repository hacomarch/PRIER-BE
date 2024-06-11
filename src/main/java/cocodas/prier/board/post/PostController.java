package cocodas.prier.board.post;

import cocodas.prier.board.post.post.request.PostRequestDto;
import cocodas.prier.board.post.post.PostService;
import cocodas.prier.board.post.post.response.PostResponseDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class PostController {

    private final PostService postService;

    // 모든 게시글 조회하기
    @GetMapping("/boards")
    @ResponseStatus(HttpStatus.OK)
    public List<PostResponseDto> allPosts() {
        return postService.allPostList();
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

    // 검색어로 모든 게시글 조회하기
//    @GetMapping("/api/boards")
//    @ResponseStatus(HttpStatus.OK)
//    public List<PostResponseDto> searchPosts(@RequestParam(value = "search") String keyword) {
//        return postService.searchPosts(keyword);
//    }

    // 게시글 작성하기
    @PostMapping("/boards")
    @ResponseStatus(HttpStatus.CREATED)
    public void addPost(@RequestHeader("Authorization") String authorizationHeader,
                        @RequestBody PostRequestDto postRequestDto) {
        String token = authorizationHeader.replace("Bearer ", "");
        postService.addPost(token, postRequestDto);
    }

    // 게시글 수정하기
    @PutMapping("/boards/{postId}")
    @ResponseStatus(HttpStatus.OK)
    public void updatePost(@RequestHeader("Authorization") String authorizationHeader,
                           @RequestBody PostRequestDto postRequestDto,
                           @PathVariable(name = "postId") Long postId) {
        String token = authorizationHeader.replace("Bearer ", "");
        postService.updatePost(token, postRequestDto, postId);
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
