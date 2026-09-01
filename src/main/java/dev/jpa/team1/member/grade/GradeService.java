package dev.jpa.team1.member.grade;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class GradeService {

    private final GradeRepository repository;

    @Transactional(readOnly = true)
    public List<GradeResponseDto> findAll() {
        return repository.findAll().stream()
                .map(GradeResponseDto::from)
                .toList();
    }

    @Transactional(readOnly = true)
    public GradeResponseDto findById(Long id) {
        Grade entity = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Grade not found. id=" + id));
        return GradeResponseDto.from(entity);
    }

    @Transactional
    public GradeResponseDto save(GradeRequestDto requestDto) {
        // TODO: Implement save logic mapping requestDto to entity
        // Grade entity = ...;
        // return GradeResponseDto.from(repository.save(entity));
        throw new UnsupportedOperationException("Save not fully implemented yet");
    }
}
