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
        String publicUrl = awsS3Service.getPublicUrl("d7cee013-a3cd-400b-8272-d3273fbefa16");

        return new ProfileImgDto(
                users.getMetadata(),
                publicUrl,
                users.getBalance()
        );
    }

    private Users findUserExist(Long userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found!"));
    }
}
