package cocodas.prier.user;

import cocodas.prier.aws.AwsS3Service;
import cocodas.prier.user.response.ProfileImgDto;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserRepository userRepository;
    private final AwsS3Service awsS3Service;

    @Value("${profile.default.s3key}")
    private String defaultProfileS3Key;

    public ProfileImgDto getProfile(Long userId) {
        Users users = findUserExist(userId);

        String metadata;
        String publicUrl;
        if (users == null) {
            metadata = "userProfile.svg";
            publicUrl = awsS3Service.getPublicUrl(defaultProfileS3Key);
        } else {
            metadata = users.getMetadata();
            publicUrl = awsS3Service.getPublicUrl(users.getS3Key());
        }

        return new ProfileImgDto(
                metadata,
                publicUrl,
                users.getBalance()
        );
    }

    private Users findUserExist(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found!"));
    }
}
