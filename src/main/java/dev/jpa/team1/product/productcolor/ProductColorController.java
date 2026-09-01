package dev.jpa.team1.product.productcolor;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product_color")
@RequiredArgsConstructor
public class ProductColorController {

    private final ProductColorService service;

    @GetMapping("/find_all")
    public List<ProductColorResponseDto> findAll() {
        return service.findAll();
    }

    @GetMapping("/product/{productId}")
    public List<ProductColorResponseDto> findByProductId(
            @PathVariable("productId") Long productId
    ) {
        return service.findByProductId(productId);
    }

    @GetMapping("/{id}")
    public ProductColorResponseDto findById(@PathVariable("id") Long id) {
        return service.findById(id);
    }

    @PostMapping("/product/{productId}/save")
    public ProductColorResponseDto save(
            @PathVariable("productId") Long productId,
            @Valid @RequestBody ProductColorRequestDto requestDto
    ) {
        return service.save(productId, requestDto);
    }

    @PutMapping("/{id}")
    public ProductColorResponseDto update(
            @PathVariable("id") Long id,
            @Valid @RequestBody ProductColorRequestDto requestDto
    ) {
        return service.update(id, requestDto);
    }


    @DeleteMapping("/{id}")
    public void delete(@PathVariable("id") Long id) {
        service.delete(id);
    }

}
