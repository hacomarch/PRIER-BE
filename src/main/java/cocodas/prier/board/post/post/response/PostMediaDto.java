package cocodas.prier.board.post.post.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostMediaDto {
    private String metadata;
    private String mediaType;
    private String s3Url;
}
