package dev.jpa.team1.beauty.skindiagnosis;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SkinDiagnosisResultRepository
        extends JpaRepository<SkinDiagnosisResult, Long> {

    List<SkinDiagnosisResult> findByMemberIdOrderByCreatedAtDesc(
            Long memberId
    );
}