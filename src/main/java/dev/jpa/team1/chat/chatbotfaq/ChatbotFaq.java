package dev.jpa.team1.chat.chatbotfaq;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "chatbot_faq")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ChatbotFaq {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chatbot_faq_seq")
    @SequenceGenerator(name = "chatbot_faq_seq", sequenceName = "chatbot_faq_seq", allocationSize = 1)
    @Column(name = "faq_id")
    private Long id;

    @Column(nullable = false)
    private Long categoryId;

    @Column(nullable = false, length = 500)
    private String question;

    @Lob
    @Column(nullable = false)
    private String answer;

    @Column(nullable = false)
    @Builder.Default
    private Integer viewCount = 0;

    @Column(nullable = false, length = 1)
    private String visible;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    private LocalDateTime updatedAt;
}
