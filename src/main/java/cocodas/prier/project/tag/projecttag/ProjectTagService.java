package cocodas.prier.project.tag.projecttag;

import cocodas.prier.project.project.Project;
import cocodas.prier.project.project.ProjectRepository;
import cocodas.prier.project.tag.tag.Tag;
import cocodas.prier.project.tag.tag.TagRepository;
import cocodas.prier.project.tag.tag.TagService;
import cocodas.prier.project.tag.tag.dto.TagDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProjectTagService {

    private final ProjectTagRepository projectTagRepository;
    private final ProjectRepository projectRepository;
    private final TagRepository tagRepository;

    private final TagService tagService;


    @Transactional
    public void linkTagsToProject(Project project, String[] tagNames) {
        for (String tagName : tagNames) {
            Long tagId = tagService.createOrGetExistingTag(tagName);
            createProjectTag(project.getProjectId(), tagId);
        }
    }

    private void createProjectTag(Long projectId, Long tagId) {
        Project project = projectRepository.findById(projectId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 프로젝트"));

        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 태그"));

        ProjectTag projectTag = new ProjectTag(tag, project);

        projectTagRepository.save(projectTag);
    }

    @Transactional
    public void deleteProjectTag(Long id) {
        projectTagRepository.deleteById(id);
    }

    @Transactional
    public void updateProjectTags(Project project, String[] updatedTags) {
        // 기존 태그 연결 모두 제거
        projectTagRepository.deleteAllByProject(project);
        project.getProjectTags().clear(); // 프로젝트 내부 태그 리스트도 클리어

        // 새로운 태그들로 프로젝트 태그 목록 재구성
        Set<String> newTagNames = new HashSet<>(Arrays.asList(updatedTags));
        for (String tagName : newTagNames) {
            Tag tag = tagRepository.findByTagName(tagName);
            if (tag == null) {
                tag = tagRepository.save(new Tag(tagName)); // 새 태그 생성 및 저장
            }
            ProjectTag projectTag = new ProjectTag(tag, project);
            project.getProjectTags().add(projectTag); // 프로젝트의 태그 목록에 추가
            projectTagRepository.save(projectTag); // 새 연결 생성
        }
    }

    public List<TagDto> getProjectTags(Project project) {
        // 태그 정보 조회
        return project.getProjectTags().stream()
                .map(projectTag -> new TagDto(
                        projectTag.getTag().getTagId(),
                        projectTag.getTag().getTagName()))
                .collect(Collectors.toList());
    }



    //태그별 프로젝트 조회
    public List<Project> findAllProjectsByTagId(Long tagId) {
        List<ProjectTag> projectTags = projectTagRepository.findByTagIdWithProject(tagId);
        return projectTags.stream()
                .map(ProjectTag::getProject)
                .collect(Collectors.toList());
    }

}
