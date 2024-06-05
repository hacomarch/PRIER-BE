package cocodas.prier.project.tag.tag;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;


@Slf4j
@Service
@RequiredArgsConstructor
public class TagService {

    private final TagRepository tagRepository;

    public Long createOrGetExistingTag(String tagName) {
        Tag existingTag = tagRepository.findByTagName(tagName);
        if (existingTag != null) {
            log.info("기존 태그 사용: {}", tagName);
            return existingTag.getTagId();
        } else {
            Tag newTag = new Tag(tagName);
            Tag savedTag = tagRepository.save(newTag);
            log.info("새 태그 생성: {}", tagName);
            return savedTag.getTagId();
        }
    }

    public void deleteTag(Long tagId) {
        tagRepository.deleteById(tagId);
        log.info("태그 삭제 완료");
    }

    public String getTagNameById(Long tagId) {
        Tag tag = tagRepository.findById(tagId)
                .orElseThrow(() -> new RuntimeException("존재하지 않는 태그"));
        return tag.getTagName();
    }

    public Long getTagIdByName(String tagName) {
        return tagRepository.findByTagName(tagName) == null ?
                null : tagRepository.findByTagName(tagName).getTagId();
    }

}
