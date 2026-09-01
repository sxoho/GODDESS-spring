package dev.jpa.team1.product.productoption;

import jakarta.validation.Valid;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product_option")
@RequiredArgsConstructor
public class ProductOptionController {

    private final ProductOptionService service;

    // 전체 옵션 조회
    @GetMapping("/find_all")
    public List<ProductOptionResponseDto> findAll() {
        return service.findAll();
    }

    // 특정 상품 옵션 조회
    @GetMapping("/product/{productId}")
    public List<ProductOptionResponseDto> findByProductId(
            @PathVariable("productId") Long productId
    ) {
        return service.findByProductId(productId);
    }

    // 특정 상품 판매 가능 옵션 조회
    @GetMapping("/product/{productId}/available")
    public List<ProductOptionResponseDto> findAvailableByProductId(
            @PathVariable("productId") Long productId
    ) {
        return service.findAvailableByProductId(productId);
    }

    // 옵션 단건 조회
    @GetMapping("/{id}")
    public ProductOptionResponseDto findById(
            @PathVariable("id") Long id
    ) {
        return service.findById(id);
    }

    // 옵션 등록
    @PostMapping("/product/{productId}/save")
    public ProductOptionResponseDto save(
            @PathVariable("productId") Long productId,
            @Valid @RequestBody ProductOptionRequestDto requestDto
    ) {
        return service.save(productId, requestDto);
    }

    // 옵션 수정
    @PutMapping("/{id}")
    public ProductOptionResponseDto update(
            @PathVariable("id") Long id,
            @Valid @RequestBody ProductOptionRequestDto requestDto
    ) {
        return service.update(id, requestDto);
    }

    // 옵션 삭제
    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable("id") Long id
    ) {
        service.delete(id);
    }
}