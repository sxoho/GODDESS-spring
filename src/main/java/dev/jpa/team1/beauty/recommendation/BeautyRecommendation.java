package dev.jpa.team1.beauty.recommendation;
import dev.jpa.team1.member.member.Member; import jakarta.persistence.*; import lombok.*; import org.hibernate.annotations.CreationTimestamp; import java.time.LocalDateTime;
@Entity @Table(name="beauty_recommendation") @Getter @NoArgsConstructor(access=AccessLevel.PROTECTED) @AllArgsConstructor @Builder
public class BeautyRecommendation {
 @Id @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="beauty_recommendation_seq") @SequenceGenerator(name="beauty_recommendation_seq",sequenceName="beauty_recommendation_seq",allocationSize=1) @Column(name="recommendation_id") private Long id;
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="member_id",nullable=false) private Member member; @Column(name="recommendation_type",length=30) private String recommendationType; @Column(length=100) private String mood; @Column(name="skin_type",length=30) private String skinType; @Column(name="personal_color",length=30) private String personalColor; @Column(name="ai_description",length=1000) private String aiDescription; @CreationTimestamp @Column(name="created_at",updatable=false) private LocalDateTime createdAt;
}
