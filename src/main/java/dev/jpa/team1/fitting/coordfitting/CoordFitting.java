package dev.jpa.team1.fitting.coordfitting;

import dev.jpa.team1.member.fittingprofile.FittingProfile;
import dev.jpa.team1.member.member.Member;
import dev.jpa.team1.member.memberphoto.MemberPhoto;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "coord_fitting")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class CoordFitting {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "coord_fitting_seq")
    @SequenceGenerator(name = "coord_fitting_seq", sequenceName = "coord_fitting_seq", allocationSize = 1)
    @Column(name = "coord_fitting_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_photo_id")
    private MemberPhoto memberPhoto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fitting_profile_id")
    private FittingProfile fittingProfile;

    @Column(name = "fitting_method", nullable = false, length = 20)
    private String fittingMethod;

    @Column(name = "top_color_id")
    private Long topColorId;

    @Column(name = "bottom_color_id")
    private Long bottomColorId;

    @Column(name = "outer_color_id")
    private Long outerColorId;

    @Column(nullable = false, length = 20)
    private String status;

    @Lob
    @Column(name = "error_message")
    private String errorMessage;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
