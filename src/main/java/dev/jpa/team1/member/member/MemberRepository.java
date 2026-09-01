package dev.jpa.team1.member.member;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface MemberRepository
        extends JpaRepository<Member, Long> {


    /**
     * 로그인 아이디 조회
     */
    Optional<Member> findByLoginId(
            String loginId
    );


    /**
     * 이메일 조회
     */
    Optional<Member> findByEmail(
            String email
    );


    /**
     * 닉네임 조회
     */
    Optional<Member> findByNickname(
            String nickname
    );


    /**
     * 아이디 중복 확인
     */
    boolean existsByLoginId(
            String loginId
    );


    /**
     * 이메일 중복 확인
     */
    boolean existsByEmail(
            String email
    );


    /**
     * 닉네임 중복 확인
     */
    boolean existsByNickname(
            String nickname
    );


    /**
     * 수정용 아이디 중복 확인
     *
     * 현재 회원 제외
     */
    boolean existsByLoginIdAndIdNot(
            String loginId,
            Long id
    );


    /**
     * 수정용 닉네임 중복 확인
     *
     * 현재 회원 제외
     */
    boolean existsByNicknameAndIdNot(
            String nickname,
            Long id
    );


    /**
     * 수정용 이메일 중복 확인
     *
     * 현재 회원 제외
     */
    boolean existsByEmailAndIdNot(
            String email,
            Long id
    );


    /**
     * 회원 상태별 조회
     *
     * ACTIVE 회원 조회
     */
    List<Member> findByStatusOrderByCreatedAtDesc(
            String status
    );


    /**
     * 등급별 조회
     */
    List<Member> findByGradeIdOrderByCreatedAtDesc(
            Long gradeId
    );


    /**
     * 이름 검색
     */
    List<Member>
    findByNameContainingIgnoreCaseOrderByCreatedAtDesc(
            String name
    );


    /**
     * 닉네임 검색
     */
    List<Member>
    findByNicknameContainingIgnoreCaseOrderByCreatedAtDesc(
            String nickname
    );


    /**
     * 전화번호 조회
     */
    Optional<Member> findByTel(
            String tel
    );


    /**
     * 전화번호 중복 확인
     */
    boolean existsByTel(
            String tel
    );


    /**
     * 전체 회원 최신 가입순
     */
    List<Member>
    findAllByOrderByCreatedAtDesc();
    
    
    /**
     * 이름과 이메일로 회원 조회
     *
     * 아이디 찾기 기능에서 사용
     */
    Optional<Member> findByNameAndEmail(
            String name,
            String email
    );
    
    /**
     * 아이디와 이메일로 회원 조회
     *
     * 비밀번호 재설정 기능에서 사용
     */
    Optional<Member> findByLoginIdAndEmail(
            String loginId,
            String email
    );
}