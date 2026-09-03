package dev.jpa.team1.beauty.tryonresult;
import dev.jpa.team1.beauty.tryon.BeautyTryon; import jakarta.persistence.*; import lombok.*; import org.hibernate.annotations.CreationTimestamp; import java.time.LocalDateTime;
@Entity @Table(name="beauty_tryon_result") @Getter @NoArgsConstructor(access=AccessLevel.PROTECTED) @AllArgsConstructor @Builder
public class BeautyTryonResult {
 @Id @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="beauty_tryon_result_seq") @SequenceGenerator(name="beauty_tryon_result_seq",sequenceName="beauty_tryon_result_seq",allocationSize=1) @Column(name="beautyresult_id") private Long id;
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="beautytryon_id",nullable=false) private BeautyTryon beautyTryon; @Column(name="file_origin",length=255) private String fileOrigin; @Column(name="file_saved",length=255) private String fileSaved; @Column(name="file_path",length=500) private String filePath; @Column(name="ai_description",length=1000) private String aiDescription; @Builder.Default @Column(nullable=false,length=1) private String success="Y"; @CreationTimestamp @Column(name="created_at",updatable=false) private LocalDateTime createdAt;
}
