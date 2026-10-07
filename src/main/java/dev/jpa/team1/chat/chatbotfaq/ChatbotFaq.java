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
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "chatbot_faq_seq"
    )
    @SequenceGenerator(
            name = "chatbot_faq_seq",
            sequenceName = "chatbot_faq_seq",
            allocationSize = 1
    )
    @Column(name = "faq_id")
    private Long id;

    @Column(nullable = false, length = 50)
    private String category;

    @Column(nullable = false, length = 500)
    private String question;

    @Lob
    @Column(nullable = false)
    private String answer;

    @Column(length = 500)
    private String keywords;

    @Column(name = "display_order", nullable = false)
    private Integer displayOrder;

    @Column(name = "view_count", nullable = false)
    @Builder.Default
    private Integer viewCount = 0;

    @Column(nullable = false, length = 1)
    @Builder.Default
    private String visible = "Y";

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;
}