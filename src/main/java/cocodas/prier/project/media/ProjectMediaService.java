package cocodas.prier.project.media;

import cocodas.prier.aws.AwsS3Service;
import cocodas.prier.project.project.Project;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectMediaService {

    private final ProjectMediaRepository projectMediaRepository;
    private final AwsS3Service awsS3Service;

    @Transactional
    public String createMainImage(Project project, MultipartFile file) throws IOException {
        return createImage(project, file, true, 0); // 메인 이미지는 항상 order 0
    }

    @Transactional
    public String createContentImage(Project project, MultipartFile[] files) throws IOException {
        int order = 1; // 내용 이미지는 1부터 시작
        for (MultipartFile file : files) {
            createImage(project, file, false, order++);
        }
        return "내용 이미지 등록 완료";
    }

    private String createImage(Project project, MultipartFile file, boolean isMain, int order) throws IOException {
        String key = handleFileUpload(file);
        ProjectMedia projectMedia = ProjectMedia.builder()
                .metadata(file.getOriginalFilename())
                .isMain(isMain)
                .s3Key(key)
                .orderIndex(order) // 순서 설정
                .project(project)
                .build();
        projectMediaRepository.save(projectMedia);
        project.getProjectMedia().add(projectMedia);

        return isMain ? "메인 이미지 등록 완료" : "이미지 등록 완료";
    }

    @Transactional
    public void deleteImage(Project project) {
        project.getProjectMedia().forEach(
                projectMedia -> awsS3Service.deleteFile(projectMedia.getS3Key())
        );
    }

    //todo : 메인 이미지 업데이트 구현해야함
    @Transactional
    public void updateMainImage(Project project, MultipartFile file) throws IOException {

    }

    @Transactional
    public void updateContentImages(Project project, MultipartFile[] files) throws IOException {

    }



    private String handleFileUpload(MultipartFile file) throws IOException {

        /*
            임시 파일을 생성.
            첫 번째 인자는 파일의 접두어, 두 번째 인자는 접미어(파일 확장자를 포함한 파일 이름)로
            시스템의 기본 임시 파일 디렉터리에 저장.
        */
        File tempFile = File.createTempFile("product-", file.getOriginalFilename());
        /*
            사용자가 업로드한 파일(MultipartFile 객체)을 위에서 생성한 임시 파일로 복사.
            이 과정을 통해 서버에 파일이 저장.
         */
        file.transferTo(tempFile);

        String key = awsS3Service.uploadFile(tempFile);
        tempFile.delete();
        return key;
    }
}
