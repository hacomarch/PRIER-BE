package cocodas.prier.statics.keywordAi;

import cocodas.prier.project.feedback.question.Category;
import cocodas.prier.project.feedback.response.Response;
import cocodas.prier.project.project.Project;
import cocodas.prier.project.project.ProjectRepository;
import cocodas.prier.statics.keywordAi.dto.response.KeyWordResponseDto;
import cocodas.prier.user.kakao.jwt.JwtTokenProvider;
import kr.co.shineware.nlp.komoran.constant.DEFAULT_MODEL;
import kr.co.shineware.nlp.komoran.core.Komoran;
import kr.co.shineware.nlp.komoran.model.Token;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Service
@RequiredArgsConstructor
public class KeywordsService {
    private final ProjectRepository projectRepository;

    public List<KeyWordResponseDto> getKeywordByProjectId(Long projectId) {
        Project project = getProjectById(projectId);

        List<String> feedbackList = getFeedbackList(project);

        Map<String, Integer> keywordCountMap = extractKeywords(feedbackList);

        List<Map.Entry<String, Integer>> sortedKeywords = getSortedKeywords(keywordCountMap);

        List<KeyWordResponseDto> responseDtos = new ArrayList<>();
        for (Map.Entry<String, Integer> entry : sortedKeywords) {
            responseDtos.add(new KeyWordResponseDto(entry.getKey(), entry.getValue()));
        }

        return responseDtos;
    }

    private static List<Map.Entry<String, Integer>> getSortedKeywords(Map<String, Integer> keywordCountMap) {
        return keywordCountMap.entrySet().stream()
                .sorted(Map.Entry.<String, Integer>comparingByValue().reversed())
                .limit(5)
                .toList();
    }

    private static List<String> getFeedbackList(Project project) {
        return project.getFeedbackQuestions()
                .stream()
                .filter(question -> question.getCategory().equals(Category.SUBJECTIVE))
                .flatMap(question -> question.getResponses().stream().map(Response::getContent))
                .toList();
    }

    private Project getProjectById(Long projectId) {
        return projectRepository.findById(projectId).orElseThrow(() -> new RuntimeException("Not Found Project"));
    }

    private Map<String, Integer> extractKeywords(List<String> feedbackList) {
        Komoran komoran = new Komoran(DEFAULT_MODEL.FULL);
        Map<String, Integer> keywordCountMap = new HashMap<>();

        for (String feedback : feedbackList) {
            List<Token> tokens = komoran.analyze(feedback).getTokenList();
            for (Token token : tokens) {
                if (token.getPos().equals("VA")) {
                    String keyword = token.getMorph() + "다";
                    keywordCountMap.put(keyword, keywordCountMap.getOrDefault(keyword, 0) + 1);
                }
            }
        }
        return keywordCountMap;
    }
}
