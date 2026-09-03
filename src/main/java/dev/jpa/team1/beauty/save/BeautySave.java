package dev.jpa.team1.beauty.save;
import dev.jpa.team1.member.member.Member; import dev.jpa.team1.beauty.tryonresult.BeautyTryonResult; import jakarta.persistence.*; import lombok.*; import org.hibernate.annotations.CreationTimestamp; import java.time.LocalDateTime;
@Entity @Table(name="beauty_save") @Getter @NoArgsConstructor(access=AccessLevel.PROTECTED) @AllArgsConstructor @Builder
public class BeautySave {
 @Id @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="beauty_save_seq") @SequenceGenerator(name="beauty_save_seq",sequenceName="beauty_save_seq",allocationSize=1) @Column(name="beautysave_id") private Long id;
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="member_id",nullable=false) private Member member; @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="beautyresult_id",nullable=false) private BeautyTryonResult beautyTryonResult; @Column(name="save_name",length=100) private String saveName; @Column(length=500) private String memo; @CreationTimestamp @Column(name="created_at",updatable=false) private LocalDateTime createdAt;
}
