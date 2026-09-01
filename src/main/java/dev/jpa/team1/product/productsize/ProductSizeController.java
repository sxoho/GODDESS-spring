package dev.jpa.team1.product.productsize;

import lombok.RequiredArgsConstructor;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product_size")
@RequiredArgsConstructor
public class ProductSizeController {

    private final ProductSizeService service;

    // 전체 사이즈 조회
    @GetMapping("/find_all")
    public List<ProductSizeResponseDto> findAll() {
        return service.findAll();
    }

    // 특정 상품 사이즈 조회
    @GetMapping("/product/{productId}")
    public List<ProductSizeResponseDto> findByProductId(
            @PathVariable("productId") Long productId
    ) {
        return service.findByProductId(productId);
    }

    // 사이즈 한 건 조회
    @GetMapping("/{id}")
    public ProductSizeResponseDto findById(
            @PathVariable("id") Long id
    ) {
        return service.findById(id);
    }

    // 특정 상품에 사이즈 등록
    @PostMapping("/product/{productId}/save")
    public ProductSizeResponseDto save(
            @PathVariable("productId") Long productId,
            @RequestBody ProductSizeRequestDto requestDto
    ) {
        return service.save(
                productId,
                requestDto
        );
    }

    // 사이즈 수정
    @PutMapping("/{id}")
    public ProductSizeResponseDto update(
            @PathVariable("id") Long id,
            @RequestBody ProductSizeRequestDto requestDto
    ) {
        return service.update(id, requestDto);
    }

    // 사이즈 삭제
    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable("id") Long id
    ) {
        service.delete(id);
    }
}