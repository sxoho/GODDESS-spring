package dev.jpa.team1.beauty.personalcolor;
import dev.jpa.team1.member.member.Member; import jakarta.persistence.*; import lombok.*; import org.hibernate.annotations.CreationTimestamp; import java.time.LocalDateTime;
@Entity @Table(name="personal_color_result") @Getter @NoArgsConstructor(access=AccessLevel.PROTECTED) @AllArgsConstructor @Builder
public class PersonalColorResult {
 @Id @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="personal_color_result_seq") @SequenceGenerator(name="personal_color_result_seq",sequenceName="personal_color_result_seq",allocationSize=1) @Column(name="personalcolor_id") private Long id;
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="member_id",nullable=false) private Member member; @Column(name="personal_color",nullable=false,length=30) private String personalColor;
 @Builder.Default @Column(name="spring_score",nullable=false) private Integer springScore=0; @Builder.Default @Column(name="summer_score",nullable=false) private Integer summerScore=0; @Builder.Default @Column(name="autumn_score",nullable=false) private Integer autumnScore=0; @Builder.Default @Column(name="winter_score",nullable=false) private Integer winterScore=0;
 @Column(name="result_description",length=1000) private String resultDescription; @CreationTimestamp @Column(name="created_at",updatable=false) private LocalDateTime createdAt;
}
