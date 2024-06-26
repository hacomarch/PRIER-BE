package cocodas.prier.board.post.post.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostMediaDto {
    private Long postMediaId;
    private String metadata;
    private String mediaType;
    private String s3Key;
    private String s3Url;
}
