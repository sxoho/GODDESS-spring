package dev.jpa.team1.member.fittingprofile;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;


/**
 * 피팅 프로필 Repository
 */
public interface FittingProfileRepository
    extends JpaRepository<FittingProfile, Long> {


    /**
     * 회원 번호로 피팅 프로필 조회
     */
    Optional<FittingProfile> findByMemberId(
        Long memberId
    );


    /**
     * 회원의 피팅 프로필 존재 여부
     */
    boolean existsByMemberId(
        Long memberId
    );
}