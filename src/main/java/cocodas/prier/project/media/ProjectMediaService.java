package cocodas.prier.project.media;

import cocodas.prier.aws.AwsS3Service;
import cocodas.prier.project.media.dto.ProjectMediaDto;
import cocodas.prier.project.project.Project;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

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
        int order = 1;
        if (files != null) {
            for (MultipartFile file : files) {
                if (file != null && !file.isEmpty()) {  // 파일이 null이 아니고, 비어있지 않은 경우에만 이미지 생성
                    createImage(project, file, false, order++);
                }
            }
        }
        return "내용 이미지 등록 완료";
    }

    private String createImage(Project project, MultipartFile file, boolean isMain, int order) throws IOException {

        //s3Key == null 일 경우 기본 이미지
        if (file == null || file.isEmpty()) {
            ProjectMedia projectMedia = ProjectMedia.builder()
                    .metadata("defaultImage.jpeg")
                    .isMain(isMain)
                    .s3Key(null)
                    .orderIndex(order) // 순서 설정
                    .project(project)
                    .build();
            projectMediaRepository.save(projectMedia);
            project.getProjectMedia().add(projectMedia);
        }
        else {
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
        }

        return isMain ? "메인 이미지 등록 완료" : "이미지 등록 완료";
    }

    @Transactional
    public void deleteImage(Project project) {
        project.getProjectMedia().forEach(projectMedia -> {
            if (projectMedia.getS3Key() != null) { // S3 키가 null이 아닌 경우에만 삭제 수행
                awsS3Service.deleteFile(projectMedia.getS3Key());
            }
        });
    }


    @Transactional
    public void updateMainImage(Project project, MultipartFile file) throws IOException {
        ProjectMedia mainMedia = project.getProjectMedia().stream()
                .filter(ProjectMedia::isMain)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("메인 이미지 존재하지 않음"));

        // 메인 이미지가 제공되지 않은 경우 기본 이미지로 교체
        if (file == null || file.isEmpty()) {
            log.info("이미지 존재 -> 기본 이미지로, 빈 이미지로 들어가는지 = {}", file.isEmpty());
            // 기존 메인 이미지가 기본 이미지가 아니라면 삭제
            if (!"defaultImage".equals(mainMedia.getMetadata())) {
                log.info("여기서 피자가 기본 이미지로 바뀔 것, 기존 이미지 = {}", mainMedia.getMetadata());
                awsS3Service.deleteFile(mainMedia.getS3Key());  // S3에서 기존 이미지 삭제
                mainMedia.setS3Key(null);  // S3 키 null 처리
                mainMedia.setMetadata("defaultImage");  // 기본 이미지로 메타데이터 설정
            }
        } else {
            // 새 이미지가 제공된 경우 기존 이미지 업데이트
            if (!mainMedia.getMetadata().equals(file.getOriginalFilename())) {
                awsS3Service.deleteFile(mainMedia.getS3Key());  // 기존 파일 S3에서 삭제

                String newS3Key = handleFileUpload(file);  // 새 파일 업로드
                mainMedia.setS3Key(newS3Key);  // 새 S3 키 설정
                mainMedia.setMetadata(file.getOriginalFilename());  // 새 메타데이터 설정
            }
        }

        projectMediaRepository.save(mainMedia);  // 변경 사항 저장
    }

    @Transactional
    public void updateContentImages(Project project, MultipartFile[] files) throws IOException {
        List<ProjectMedia> contentImages = project.getProjectMedia().stream()
                .filter(media -> !media.isMain())
                .toList();

        Map<String, ProjectMedia> contentImagesMap = contentImages.stream()
                .collect(Collectors.toMap(ProjectMedia::getMetadata, media -> media));

        Set<String> fileNames = Arrays.stream(files)
                .map(MultipartFile::getOriginalFilename)
                .collect(Collectors.toSet());

        // 기존에 없는 새 파일 업로드
        int index = 1;
        for (MultipartFile file : files) {
            if (!file.isEmpty()) {
                ProjectMedia media = contentImagesMap.get(file.getOriginalFilename());
                if (media == null) {
                    String newS3Key = handleFileUpload(file);
                    ProjectMedia newMedia = ProjectMedia.builder()
                            .metadata(file.getOriginalFilename())
                            .isMain(false)
                            .s3Key(newS3Key)
                            .orderIndex(index)
                            .project(project)
                            .build();
                    projectMediaRepository.save(newMedia);
                } else {
                    // 순서 업데이트만 수행
                    if (media.getOrderIndex() != index) {
                        media.setOrderIndex(index);
                        projectMediaRepository.save(media);
                    }
                }
            }
            index++;
        }

        // 제거된 파일 삭제
        contentImages.forEach(media -> {
            if (!fileNames.contains(media.getMetadata())) {
                awsS3Service.deleteFile(media.getS3Key());
                project.getProjectMedia().remove(media);
                projectMediaRepository.delete(media);
            }
        });
    }


    private String handleFileUpload(MultipartFile file) throws IOException {

        /*
            임시 파일을 생성.
            첫 번째 인자는 파일의 접두어, 두 번째 인자는 접미어(파일 확장자를 포함한 파일 이름)로
            시스템의 기본 임시 파일 디렉터리에 저장.
        */
        File tempFile = File.createTempFile("project-", file.getOriginalFilename());
        /*
            사용자가 업로드한 파일(MultipartFile 객체)을 위에서 생성한 임시 파일로 복사.
            이 과정을 통해 서버에 파일이 저장.
         */
        file.transferTo(tempFile);

        String key = awsS3Service.uploadFile(tempFile);
        tempFile.delete();
        return key;
    }

    public List<ProjectMediaDto> getProjectDetailMedia(Project project) {
        return project.getProjectMedia().stream()
                .map(media -> new ProjectMediaDto(
                        media.getProjectMediaId(),
                        media.getMetadata(),
                        media.isMain(),
                        media.getMediaType().name(),
                        getS3Url(media.getS3Key()),
                        media.getOrderIndex()))
                .collect(Collectors.toList());
    }

    public String getMainImageUrl(Project project) {
        String s3Key = project.getProjectMedia().stream()
                .filter(ProjectMedia::isMain)
                .findFirst()
                .map(ProjectMedia::getS3Key)
                .orElse(null);
        return getS3Url(s3Key);
    }

    public String getS3Url(String s3Key) {
        if (s3Key == null) {
            return "/images/default-image.jpg";
        }
        return awsS3Service.getPublicUrl(s3Key);
    }

}
