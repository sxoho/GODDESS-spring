package dev.jpa.team1.chat.chatbotfaq;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ChatbotFaqRequestDto {
    @NotNull
    private Long categoryId;

    @NotBlank
    @Size(max = 500)
    private String question;

    @NotBlank
    private String answer;

    @NotBlank
    @Size(max = 1)
    private String visible;
}
