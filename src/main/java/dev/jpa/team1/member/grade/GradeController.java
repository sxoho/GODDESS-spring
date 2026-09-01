package dev.jpa.team1.member.grade;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/grade")
@RequiredArgsConstructor
public class GradeController {

    private final GradeService service;

    @GetMapping("/find_all")
    public List<GradeResponseDto> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public GradeResponseDto findById(@PathVariable Long id) {
        return service.findById(id);
    }

    @PostMapping("/save")
    public GradeResponseDto save(@RequestBody GradeRequestDto requestDto) {
        return service.save(requestDto);
    }
}
