package dev.jpa.team1.fitting.fittingsave;

import dev.jpa.team1.fitting.coordfittingresult.CoordFittingResult;
import dev.jpa.team1.fitting.quickfittingresult.QuickFittingResult;
import dev.jpa.team1.member.member.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


@Entity
@Table(name = "fitting_save")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class FittingSave {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "fitting_save_seq")
    @SequenceGenerator(name = "fitting_save_seq", sequenceName = "fitting_save_seq", allocationSize = 1)
    @Column(name = "fitting_save_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @Column(name = "fitting_type", nullable = false, length = 50)
    private String fittingType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quick_fitting_result_id")
    private QuickFittingResult quickFittingResult;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coord_fitting_result_id")
    private CoordFittingResult coordFittingResult;

    @Column(name = "save_name", length = 100)
    private String saveName;

    @Lob
    private String memo;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
