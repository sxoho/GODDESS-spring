package dev.jpa.team1.board.notice;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;


public interface NoticeRepository
        extends JpaRepository<Notice, Long> {


    /**
     * 관리자 공지사항 전체 목록
     *
     * 고정 공지 우선
     * 그 안에서는 최신순
     */
    List<Notice>
    findAllByOrderByFixedDescCreatedAtDesc();


    /**
     * 일반 사용자 공개 공지사항 목록
     *
     * visible = Y만 조회
     * fixed = Y 먼저
     * 최신순
     */
    List<Notice>
    findByVisibleOrderByFixedDescCreatedAtDesc(
            String visible
    );


    /**
     * 작성 관리자별 조회
     */
    List<Notice>
    findByAdminIdOrderByCreatedAtDesc(
            Long adminId
    );


    /**
     * 관리자 제목 검색
     */
    List<Notice>
    findByTitleContainingIgnoreCaseOrderByCreatedAtDesc(
            String title
    );


    /**
     * 공개 공지 제목 검색
     */
    List<Notice>
    findByVisibleAndTitleContainingIgnoreCaseOrderByCreatedAtDesc(
            String visible,
            String title
    );
}