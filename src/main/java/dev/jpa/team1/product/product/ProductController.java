package dev.jpa.team1.product.product;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService service;

    // 전체 조회
    @GetMapping("/find_all")
    public List<ProductResponseDto> findAll() {
        return service.findAll();
    }

    // 관리자 상품 페이징 조회
    // 판매중 / 품절 / 판매중지 모두 포함
    @GetMapping("/admin/page")
    public Page<ProductResponseDto> findAdminPage(

            @RequestParam(name = "page", defaultValue = "0")
            int page,

            @RequestParam(name = "size", defaultValue = "10")
            int size,

            @RequestParam(name = "mainCategoryId", required = false)
            Long mainCategoryId,

            @RequestParam(name = "categoryId", required = false)
            Long categoryId,

            @RequestParam(name = "sort", defaultValue = "new")
            String sort,

            @RequestParam(name = "keyword", required = false)
            String keyword
    ) {

        return service.findAdminPage(
                page,
                size,
                mainCategoryId,
                categoryId,
                sort,
                keyword
        );
    }

    // 사용자 상품 페이징 조회
    // 판매중지 상품은 제외
    @GetMapping("/page")
    public Page<ProductResponseDto> findPage(

            @RequestParam(name = "page", defaultValue = "0")
            int page,

            @RequestParam(name = "size", defaultValue = "24")
            int size,

            @RequestParam(name = "mainCategoryId", required = false)
            Long mainCategoryId,

            @RequestParam(name = "categoryId", required = false)
            Long categoryId,

            @RequestParam(name = "sort", defaultValue = "recommended")
            String sort,

            @RequestParam(name = "keyword", required = false)
            String keyword,

            // PRODUCT 추천 필터
            // React ProductList에서 comma-separated 값으로 전달될 수 있다.
            // 예: skinType=건성,복합성 / personalColor=봄웜,여름쿨
            @RequestParam(name = "skinType", required = false)
            String skinType,

            @RequestParam(name = "personalColor", required = false)
            String personalColor,

            @RequestParam(name = "aiApplyEnabled", required = false)
            String aiApplyEnabled
    ) {

        return service.findPage(
                page,
                size,
                mainCategoryId,
                categoryId,
                sort,
                keyword,
                skinType,
                personalColor,
                aiApplyEnabled
        );
    }

    // BEST 상품
    @GetMapping("/best")
    public List<ProductResponseDto> findBest(
            @RequestParam(name = "keyword", required = false)
            String keyword
    ) {
        return service.findBest100(keyword);
    }

    // 단건 조회
    @GetMapping("/{id}")
    public ProductResponseDto findById(
            @PathVariable("id") Long id
    ) {
        return service.findById(id);
    }

    // 등록
    @PostMapping("/save")
    public ProductResponseDto save(
            @RequestBody ProductRequestDto requestDto
    ) {
        return service.save(requestDto);
    }

    // 수정
    @PutMapping("/{id}")
    public ProductResponseDto update(
            @PathVariable("id") Long id,
            @RequestBody ProductRequestDto requestDto
    ) {
        return service.update(id, requestDto);
    }

    // 삭제 처리
    // 실제 행 삭제가 아니라 판매중지로 변경
    @DeleteMapping("/{id}")
    public void delete(
            @PathVariable("id") Long id
    ) {
        service.stopSale(id);
    }
    
    //판매재개
    @PutMapping("/{id}/resume")
    public void resumeSale(
            @PathVariable("id") Long id
    ) {
        service.resumeSale(id);
    }
}