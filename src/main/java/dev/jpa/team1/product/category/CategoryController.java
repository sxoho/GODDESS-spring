package dev.jpa.team1.product.category;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/category")
@RequiredArgsConstructor
public class CategoryController {

    private final CategoryService service;

    // 쇼핑몰용 전체 카테고리 조회
    @GetMapping("/find_all")
    public List<CategoryResponseDto> findAll() {
        return service.findAll();
    }

    // 관리자용 대분류 + 하위분류 계층형 조회
    @GetMapping("/tree")
    public List<CategoryResponseDto> findTree() {
        return service.findTree();
    }

    // 카테고리 단건 조회
    @GetMapping("/{id}")
    public CategoryResponseDto findById(
            @PathVariable(name = "id") Long id
    ) {
        return service.findById(id);
    }

    // 카테고리 등록
    @PostMapping("/save")
    public CategoryResponseDto save(
            @Valid @RequestBody CategoryRequestDto requestDto
    ) {
        return service.save(requestDto);
    }

    // 카테고리 수정
    @PutMapping("/{id}")
    public CategoryResponseDto update(
            @PathVariable(name = "id") Long id,
            @Valid @RequestBody CategoryRequestDto requestDto
    ) {
        return service.update(id, requestDto);
    }

    // 카테고리 삭제
    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable(name = "id") Long id
    ) {
        service.delete(id);
    }
}
