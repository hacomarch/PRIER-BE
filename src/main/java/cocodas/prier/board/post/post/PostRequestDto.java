package cocodas.prier.board.post.post;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostRequestDto {

    // category 를 ENUM 으로 만들까?
    private String title;
    private String content;
    private Category category;

}
