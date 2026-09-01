package dev.jpa.team1.fitting.quickfittingresult;

import dev.jpa.team1.fitting.quickfitting.QuickFitting;
import dev.jpa.team1.product.productcolor.ProductColor;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


@Entity
@Table(name = "quick_fitting_result")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class QuickFittingResult {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "quick_fitting_result_seq")
    @SequenceGenerator(name = "quick_fitting_result_seq", sequenceName = "quick_fitting_result_seq", allocationSize = 1)
    @Column(name = "quick_fitting_result_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quick_fitting_id", nullable = false)
    private QuickFitting quickFitting;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_color_id", nullable = false)
    private ProductColor productColor;

    @Column(name = "file_origin", length = 255)
    private String fileOrigin;

    @Column(name = "file_saved", length = 255)
    private String fileSaved;

    @Column(name = "file_path", length = 500)
    private String filePath;

    @Column(name = "model_file_path", length = 1000)
    private String modelFilePath;

    @Lob
    @Column(name = "ai_description")
    private String aiDescription;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false, length = 1)
    private String success;
}
