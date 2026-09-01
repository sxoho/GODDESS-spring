package dev.jpa.team1.member.loginhistory;

import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;


public interface LoginHistoryRepository
        extends JpaRepository<LoginHistory, Long> {


    /**
     * 회원별 로그인 이력 조회
     * 최근 로그인 순
     */
    List<LoginHistory>
    findByMemberIdOrderByCreatedAtDesc(
            Long memberId
    );


    /**
     * 회원별 로그인 성공 여부에 따른 이력 조회
     */
    List<LoginHistory>
    findByMemberIdAndSuccessOrderByCreatedAtDesc(
            Long memberId,
            String success
    );


    /**
     * 전체 로그인 성공 또는 실패 기록 조회
     */
    List<LoginHistory>
    findBySuccessOrderByCreatedAtDesc(
            String success
    );


    /**
     * 특정 기간의 회원 로그인 이력 조회
     */
    List<LoginHistory>
    findByMemberIdAndCreatedAtBetweenOrderByCreatedAtDesc(
            Long memberId,
            LocalDateTime startDate,
            LocalDateTime endDate
    );


    /**
     * 특정 IP의 로그인 이력 조회
     */
    List<LoginHistory>
    findByIpOrderByCreatedAtDesc(
            String ip
    );


    /**
     * 회원의 최근 로그인 이력 한 건 조회
     */
    LoginHistory
    findFirstByMemberIdOrderByCreatedAtDesc(
            Long memberId
    );


    /**
     * 아직 로그아웃하지 않은
     * 최근 성공 로그인 이력 조회
     */
    Optional<LoginHistory>
    findFirstByMemberIdAndSuccessAndLogoutDateIsNullOrderByCreatedAtDesc(
            Long memberId,
            String success
    );
}