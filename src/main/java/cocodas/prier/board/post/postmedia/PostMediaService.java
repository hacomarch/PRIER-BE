package cocodas.prier.board.post.postmedia;

import cocodas.prier.aws.AwsS3Service;
import cocodas.prier.board.post.post.Post;
import cocodas.prier.board.post.post.response.PostMediaDto;
import cocodas.prier.project.media.MediaType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Comparator;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PostMediaService {
    private final PostMediaRepository postMediaRepository;
    private final AwsS3Service awsS3Service;

    public List<PostMediaDto> getPostMediaDetail(Post post) {
        return post.getPostMedia().stream()
                .sorted(Comparator.comparing(PostMedia::getPostMediaId)) // PostMedia의 ID를 기준으로 정렬
                .map(media -> new PostMediaDto(
                        media.getPostMediaId(),
                        media.getMetadata(),
                        media.getMediaType().name(),
                        media.getS3Key(),
                        getS3Url(media.getS3Key())
                ))
                .toList();
    }


    private String getS3Url(String s3Key) {
        return awsS3Service.getPublicUrl(s3Key);
    }

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

    @Transactional
    public void updateFile(Long[] postMediaIds, Post post, MultipartFile[] files) throws IOException {

        if (postMediaIds != null) {
            for (Long mediaId : postMediaIds) {
                PostMedia postMedia = postMediaRepository.findById(mediaId).orElseThrow(() -> new RuntimeException("해당 게시글에 알맞는 사진이 없습니다."));
                awsS3Service.deleteFile(postMedia.getS3Key());
                postMediaRepository.delete(postMedia);
                post.getPostMedia().remove(postMedia);
            }
        }

        if (files != null) {
            for (MultipartFile file : files) {

                File tempFile = File.createTempFile("board-", file.getOriginalFilename());
                file.transferTo(tempFile);
                String s3Key = awsS3Service.uploadFile(tempFile);

                PostMedia postMedia = PostMedia.builder()
                        .metadata(file.getOriginalFilename())
                        .s3Key(s3Key)
                        .mediaType(MediaType.IMAGE)
                        .post(post)
                        .build();

                postMediaRepository.save(postMedia);
            }
        }
    }

    @Transactional
    public void deleteFile(Post post) {
        postMediaRepository.findByPost_PostId(post.getPostId())
                .forEach(media -> {
                    awsS3Service.deleteFile(media.getS3Key());
                    postMediaRepository.delete(media);
                });
        postMediaRepository.flush();
    }
}
