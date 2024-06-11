package cocodas.prier.board.post.postmedia;

import cocodas.prier.aws.AwsS3Service;
import cocodas.prier.board.post.post.Post;
import cocodas.prier.board.post.post.PostRepository;
import cocodas.prier.project.media.MediaType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostMediaService {
    private final PostMediaRepository postMediaRepository;
    private final PostRepository postRepository;
    private final AwsS3Service awsS3Service;

    @Transactional
    public void uploadFile(Post post, MultipartFile[] files) throws IOException {
        if (files != null) {
            for (MultipartFile file : files) {
                if (file != null && !file.isEmpty()) {
                    saveMedia(post, file);
                }
            }
        }
    }

    private void saveMedia(Post post, MultipartFile file) throws IOException {
        String key = awsFileUploadAndGetKey(file);
        PostMedia postMedia = PostMedia.builder()
                .metadata(file.getOriginalFilename())
                .s3Key(key)
                .mediaType(MediaType.IMAGE)
                .post(post)
                .build();

        postMediaRepository.save(postMedia);
    }

    private String awsFileUploadAndGetKey(MultipartFile file) throws IOException {
        File tempFile = File.createTempFile("post-", file.getOriginalFilename());
        file.transferTo(tempFile);

        String key = awsS3Service.uploadFile(tempFile);
        tempFile.delete();
        return key;
    }

    //TODO : DB에서 삭제되지 않고 추가됨
    @Transactional
    public void updateFile(Post post, MultipartFile[] files) throws IOException {
        deleteFile(post);
        uploadFile(post, files);
    }

    //TODO : s3에서 삭제되지 않음
    @Transactional
    public void deleteFile(Post post) {
        post.getPostMedia()
                .forEach(media -> awsS3Service.deleteFile(media.getS3Key()));
    }
}
