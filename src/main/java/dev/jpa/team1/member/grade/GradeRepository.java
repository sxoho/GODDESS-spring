package dev.jpa.team1.member.grade;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface GradeRepository extends JpaRepository<Grade, Long> {

    /**
     * 등급 이름으로 등급 조회
     * 예: USER, ADMIN, VIP
     */
    Optional<Grade> findByName(String name);

    /**
     * 등급 이름 중복 여부 확인
     */
    boolean existsByName(String name);

    /**
     * 공개 여부에 따른 등급 목록 조회
     * 예: visible = "Y"
     */
    List<Grade> findByVisibleOrderByIdAsc(String visible);

    /**
     * 전체 등급 목록 정렬 조회
     *
     * 관리자 화면에서 등급 관리 시 사용한다.
     */
    List<Grade> findAllByOrderByIdAsc();
}