package dev.jpa.team1.member.fittingprofile;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import dev.jpa.team1.member.member.Member;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.SequenceGenerator;
import jakarta.persistence.Table;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;


/**
 * 회원 피팅 프로필
 *
 * AI FITTING에서 사용할
 * 회원의 기본 체형 / 사이즈 / 선호 핏 정보를 저장한다.
 */
@Entity
@Table(name = "fitting_profile")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class FittingProfile {

    /**
     * 피팅 프로필 번호
     */
    @Id
    @GeneratedValue(
        strategy = GenerationType.SEQUENCE,
        generator = "fitting_profile_seq"
    )
    @SequenceGenerator(
        name = "fitting_profile_seq",
        sequenceName = "fitting_profile_seq",
        allocationSize = 1
    )
    @Column(name = "fittingprofile_id")
    private Long id;


    /**
     * 회원
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
        name = "member_id",
        nullable = false
    )
    private Member member;


    /**
     * 키
     *
     * Oracle:
     * NUMBER(5,1)
     *
     * 예:
     * 165.5
     */
    @Column(
        name = "height",
        precision = 5,
        scale = 1
    )
    private BigDecimal height;


    /**
     * 체형
     */
    @Column(
        name = "body_type",
        length = 50
    )
    private String bodyType;


    /**
     * 평소 상의 사이즈
     */
    @Column(
        name = "top_size",
        length = 20
    )
    private String topSize;


    /**
     * 평소 하의 사이즈
     */
    @Column(
        name = "bottom_size",
        length = 20
    )
    private String bottomSize;


    /**
     * 선호 핏
     */
    @Column(
        name = "preferred_fit",
        length = 50
    )
    private String preferredFit;


    /**
     * 등록일
     */
    @CreationTimestamp
    @Column(
        name = "created_at",
        nullable = false,
        updatable = false
    )
    private LocalDateTime createdAt;


    /**
     * 수정일
     */
    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


    /**
     * 피팅 프로필 수정
     */
    public void update(
        BigDecimal height,
        String bodyType,
        String topSize,
        String bottomSize,
        String preferredFit
    ) {

        this.height = height;
        this.bodyType = bodyType;
        this.topSize = topSize;
        this.bottomSize = bottomSize;
        this.preferredFit = preferredFit;
    }
}