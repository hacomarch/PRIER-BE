package cocodas.prier.user;

import cocodas.prier.aws.AwsS3Service;
import cocodas.prier.user.response.ProfileImgDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserProfileService {

    private final UserRepository userRepository;
    private final AwsS3Service awsS3Service;

    public ProfileImgDto getProfile(Long userId) {
        Users users = findUserExist(userId);
        String publicUrl = awsS3Service.getPublicUrl("936038af-8939-484b-b0e2-8ce72bf38d13");

        String metadata;
        if (users == null) {
            metadata = "userProfile.svg";
        } else {
            metadata = users.getMetadata();
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
