package cocodas.prier.statics.chatgpt.request;

import lombok.Getter;

import java.util.List;

@Getter
public class ChatGPTRequest {
    private final String model = "gpt-3.5-turbo";
    private List<Message> messages;

    public ChatGPTRequest(String prompt) {
        this.messages = List.of(
                new Message("system", "You are a helpful assistant designed to output JSON."),
                new Message("user", "다음 피드백을 두 문장으로 요약해 주세요, " +
                        "두 문장을 한 문장으로 붙여서 피드백된 문장만 String 형식으로 보여주세요: " + prompt)
        );
    }

    @Getter
    public static class Message {
        private String role;
        private String content;

        public Message(String role, String content) {
            this.role = role;
            this.content = content;
        }
    }
}
