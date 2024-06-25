package cocodas.prier.statics.chatgpt;

import cocodas.prier.project.feedback.question.Category;
import cocodas.prier.project.feedback.question.Question;
import cocodas.prier.project.feedback.response.Response;
import cocodas.prier.project.project.ProjectRepository;
import cocodas.prier.statics.chatgpt.request.ChatGPTRequest;
import cocodas.prier.statics.chatgpt.response.ChatGPTResponse;
import cocodas.prier.statics.chatgpt.response.SummaryResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatGPTService {

    private final WebClient.Builder webClientBuilder;
    private final ProjectRepository projectRepository;

    @Value("${openai.api.key}")
    private String CHATGPT_API_KEY;

    public List<SummaryResponse> getChatGptResponse(Long projectId) {
        List<Question> questions = getSubjectiveQuestions(projectId);

        WebClient webClient = createWebClient();

        List<Mono<SummaryResponse>> responseMonos = questions.stream()
                .map(question -> createSummaryMono(webClient, question))
                .toList();

        return Flux.merge(responseMonos)
                .collectList()
                .block();
    }

    private List<Question> getSubjectiveQuestions(Long projectId) {
        return projectRepository.findWithQuestionsAndResponsesByProjectId(projectId)
                .map(project -> project.getFeedbackQuestions().stream()
                        .filter(question -> question.getCategory().equals(Category.SUBJECTIVE))
                        .collect(Collectors.toList()))
                .orElse(Collections.emptyList());
    }

    private WebClient createWebClient() {
        return webClientBuilder.baseUrl("https://api.openai.com")
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + CHATGPT_API_KEY)
                .defaultHeader(HttpHeaders.CONTENT_TYPE, "application/json")
                .build();
    }

    private Mono<SummaryResponse> createSummaryMono(WebClient webClient, Question question) {
        String prompt = question
                .getResponses()
                .stream()
                .map(Response::getContent)
                .collect(Collectors.joining(" "));
        return sendChatGptRequest(webClient, prompt)
                .map(response -> new SummaryResponse(question.getQuestionId(),
                        question.getContent(),
                        question.getResponses().size(),
                        response));
    }

    private Mono<String> sendChatGptRequest(WebClient webClient, String prompt) {
        return webClient.post()
                .uri("/v1/chat/completions")
                .bodyValue(new ChatGPTRequest(prompt))
                .retrieve()
                .bodyToMono(ChatGPTResponse.class)
                .map(ChatGPTResponse::getResponseText);
    }
}
