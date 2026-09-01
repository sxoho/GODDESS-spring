package dev.jpa.team1.recommend.stylerecommendation;

import dev.jpa.team1.member.member.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "style_recommendation")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class StyleRecommendation {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "style_recommendation_seq")
    @SequenceGenerator(name = "style_recommendation_seq", sequenceName = "style_recommendation_seq", allocationSize = 1)
    @Column(name = "recommendation_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(nullable = false, length = 100)
    private String occasion;

    @Column(nullable = false, length = 100)
    private String mood;

    @Lob
    @Column(name = "ai_description")
    private String aiDescription;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
