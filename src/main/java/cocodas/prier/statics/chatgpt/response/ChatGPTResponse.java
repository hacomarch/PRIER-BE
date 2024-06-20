package cocodas.prier.statics.chatgpt.response;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class ChatGPTResponse {
    private List<Choice> choices;

    @JsonCreator
    public ChatGPTResponse(@JsonProperty("choices") List<Choice> choices) {
        this.choices = choices;
    }

    @Getter
    @Setter
    @NoArgsConstructor
    public static class Choice {
        private Message message;

        @JsonCreator
        public Choice(@JsonProperty("message") Message message) {
            this.message = message;
        }

        @Getter
        @Setter
        @NoArgsConstructor
        public static class Message {
            private String role;
            private String content;

            @JsonCreator
            public Message(@JsonProperty("role") String role, @JsonProperty("content") String content) {
                this.role = role;
                this.content = content;
            }
        }
    }

    public String getResponseText() {
        StringBuilder responseText = new StringBuilder();
        for (Choice choice : choices) {
            responseText.append(choice.getMessage().getContent()).append("\n");
        }
        return responseText.toString().trim();
    }
}
