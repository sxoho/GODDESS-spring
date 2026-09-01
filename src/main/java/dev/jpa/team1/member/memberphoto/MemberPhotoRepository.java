package dev.jpa.team1.member.memberphoto;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;


public interface MemberPhotoRepository
        extends JpaRepository<MemberPhoto, Long> {


    /**
     * 회원 전체 사진 조회
     * 최신순
     */
    List<MemberPhoto>
    findByMemberIdOrderByCreatedAtDesc(
            Long memberId
    );


    /**
     * 회원의 사용 가능한 사진 조회
     */
    List<MemberPhoto>
    findByMemberIdAndVisibleOrderByCreatedAtDesc(
            Long memberId,
            String visible
    );


    /**
     * 회원 대표 사진 조회
     */
    Optional<MemberPhoto>
    findFirstByMemberIdAndIsMainAndVisibleOrderByCreatedAtDesc(
            Long memberId,
            String isMain,
            String visible
    );


    /**
     * 회원의 FACE 사진 조회
     */
    List<MemberPhoto>
    findByMemberIdAndPhotoTypeAndVisibleOrderByCreatedAtDesc(
            Long memberId,
            String photoType,
            String visible
    );


    /**
     * 회원 사진 전체 삭제
     */
    void deleteByMemberId(
            Long memberId
    );
}