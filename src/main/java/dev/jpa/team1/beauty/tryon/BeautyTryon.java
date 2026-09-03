package dev.jpa.team1.beauty.tryon;
import dev.jpa.team1.member.member.Member; import dev.jpa.team1.member.memberphoto.MemberPhoto; import jakarta.persistence.*; import lombok.*; import org.hibernate.annotations.CreationTimestamp; import java.time.LocalDateTime;
@Entity @Table(name="beauty_tryon") @Getter @NoArgsConstructor(access=AccessLevel.PROTECTED) @AllArgsConstructor @Builder
public class BeautyTryon {
 @Id @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="beauty_tryon_seq") @SequenceGenerator(name="beauty_tryon_seq",sequenceName="beauty_tryon_seq",allocationSize=1) @Column(name="beautytryon_id") private Long id;
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="member_id",nullable=false) private Member member; @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="memberphoto_id",nullable=false) private MemberPhoto memberPhoto; @Column(name="beauty_type",nullable=false,length=30) private String beautyType; @Builder.Default @Column(nullable=false,length=20) private String status="PENDING"; @Column(name="error_message",length=500) private String errorMessage; @CreationTimestamp @Column(name="created_at",updatable=false) private LocalDateTime createdAt; @Column(name="last_view_date") private LocalDateTime lastViewDate;
 public void updateStatus(String status,String error){this.status=status;this.errorMessage=error;} public void viewed(){this.lastViewDate=LocalDateTime.now();}
}
