package cocodas.prier.user.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfileImgDto {
    private String metadata;
    private String s3Key;
    private Integer balance;
}
