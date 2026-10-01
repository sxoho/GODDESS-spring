package dev.jpa.team1.beauty.save;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface BeautySaveRepository
        extends JpaRepository<BeautySave, Long> {


    /**
     * 회원이 저장한 AI BEAUTY 결과
     * 최신 저장순
     */
    List<BeautySave>
    findByMemberIdOrderByCreatedAtDesc(
            Long memberId
    );


    /**
     * 동일 결과 중복 저장 방지
     */
    boolean existsByMemberIdAndBeautyTryonResultId(
            Long memberId,
            Long beautyResultId
    );
}