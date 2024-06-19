package cocodas.prier.board.post.post.request;

import cocodas.prier.board.post.post.Category;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class PostRequestDto {

    private String title;
    private String content;
    private Category category;
    private String[] deleteImages;

}
