package cocodas.prier.project.tag.projecttag;

import cocodas.prier.project.project.Project;
import cocodas.prier.project.project.ProjectRepository;
import cocodas.prier.project.tag.tag.Tag;
import cocodas.prier.project.tag.tag.TagRepository;
import cocodas.prier.project.tag.tag.TagService;
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
        List<ProjectTag> currentTags = projectTagRepository.findByProjectId(project.getProjectId());

        Set<String> currentTagsNames = currentTags.stream()
                .map(projectTag -> projectTag.getTag().getTagName())
                .collect(Collectors.toSet());

        Set<String> newTagNames = new HashSet<>(Arrays.asList(updatedTags));

        currentTags.forEach(projectTag -> {
            if (!newTagNames.contains(projectTag.getTag().getTagName())) {
                projectTagRepository.delete(projectTag);
            }
        });

        newTagNames.forEach(tagName -> {
            if (!currentTagsNames.contains(tagName)) {
                Tag tag = tagRepository.findByTagName(tagName);
                if (tag == null) {
                    tagRepository.save(new Tag(tagName));
                }
                projectTagRepository.save(new ProjectTag(tag, project));
            }
        });

    }


    //태그별 프로젝트 조회
    public List<Project> findAllProjectsByTagId(Long tagId) {
        List<ProjectTag> projectTags = projectTagRepository.findByTagIdWithProject(tagId);
        return projectTags.stream()
                .map(ProjectTag::getProject)
                .collect(Collectors.toList());
    }

}
