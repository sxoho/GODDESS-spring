package dev.jpa.team1.fitting.coordfittingresult;

import dev.jpa.team1.fitting.coordfitting.CoordFitting;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


@Entity
@Table(name = "coord_fitting_result")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CoordFittingResult {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "coord_fitting_result_seq")
    @SequenceGenerator(name = "coord_fitting_result_seq", sequenceName = "coord_fitting_result_seq", allocationSize = 1)
    @Column(name = "coord_fitting_result_id")
    private Long id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coord_fitting_id", nullable = false)
    private CoordFitting coordFitting;

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

    @Column(nullable = false, length = 1)
    private String success;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
