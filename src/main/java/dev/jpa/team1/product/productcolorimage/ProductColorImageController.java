package dev.jpa.team1.product.productcolorimage;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/product_color_image")
@RequiredArgsConstructor
public class ProductColorImageController {

    private final ProductColorImageService service;

    // 전체 이미지 조회
    @GetMapping("/find_all")
    public List<ProductColorImageResponseDto> findAll() {
        return service.findAll();
    }

    // 이미지 단건 조회
    @GetMapping("/{id}")
    public ProductColorImageResponseDto findById(
            @PathVariable("id") Long id
    ) {
        return service.findById(id);
    }

    // 특정 상품 색상에 이미지 저장
    @PostMapping(
            value = "/product_color/{productColorId}/save",
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    public ProductColorImageResponseDto save(
            @PathVariable("productColorId") Long productColorId,
            @RequestParam("file") MultipartFile file,
            @RequestParam("fileName") String fileName,
            @RequestParam(value = "isMain", defaultValue = "N") String isMain,
            @RequestParam(value = "imageType", defaultValue = "DETAIL") String imageType,
            @RequestParam(value = "sortOrder", defaultValue = "1") Integer sortOrder
    ) throws IOException {

        return service.save(
                productColorId,
                file,
                fileName,
                isMain,
                imageType,
                sortOrder
        );
    }


    // 이미지 메타정보 수정 (대표 여부 / 이미지 종류 / 순서)
    @PutMapping("/{id}")
    public ProductColorImageResponseDto update(
            @PathVariable("id") Long id,
            @RequestBody ProductColorImageUpdateDto requestDto
    ) {
        return service.update(
                id,
                requestDto.getIsMain(),
                requestDto.getImageType(),
                requestDto.getSortOrder()
        );
    }

    // 이미지 삭제 (DB + 실제 파일)
    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) throws IOException {
        service.delete(id);
    }

}
