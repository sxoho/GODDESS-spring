package dev.jpa.team1.beauty.skindiagnosis;
import dev.jpa.team1.member.member.Member; import jakarta.persistence.*; import lombok.*; import org.hibernate.annotations.CreationTimestamp; import java.time.LocalDateTime;
@Entity @Table(name="skin_diagnosis_result") @Getter @NoArgsConstructor(access=AccessLevel.PROTECTED) @AllArgsConstructor @Builder
public class SkinDiagnosisResult {
 @Id @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="skin_diagnosis_result_seq") @SequenceGenerator(name="skin_diagnosis_result_seq",sequenceName="skin_diagnosis_result_seq",allocationSize=1) @Column(name="skindiagnosis_id") private Long id;
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="member_id",nullable=false) private Member member;
 @Column(name="skin_type",nullable=false,length=30) private String skinType;
 @Builder.Default @Column(name="dry_score",nullable=false) private Integer dryScore=0; @Builder.Default @Column(name="oily_score",nullable=false) private Integer oilyScore=0; @Builder.Default @Column(name="combination_score",nullable=false) private Integer combinationScore=0; @Builder.Default @Column(name="normal_score",nullable=false) private Integer normalScore=0; @Builder.Default @Column(name="sensitive_score",nullable=false) private Integer sensitiveScore=0;
 @Column(name="result_description",length=1000) private String resultDescription; @CreationTimestamp @Column(name="created_at",updatable=false) private LocalDateTime createdAt;
}
