package dev.jpa.team1.product.productvariant;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/product_variant")
@RequiredArgsConstructor
public class ProductVariantController {
    private final ProductVariantService service;
    @GetMapping("/product/{productId}") public ResponseEntity<List<ProductVariantResponseDto>> find(@PathVariable Long productId) { return ResponseEntity.ok(service.findByProductId(productId)); }
    @PostMapping("/product/{productId}/save") public ResponseEntity<ProductVariantResponseDto> save(@PathVariable Long productId, @Valid @RequestBody ProductVariantRequestDto dto) { return ResponseEntity.ok(service.save(productId, dto)); }
    @PutMapping("/{id}") public ResponseEntity<ProductVariantResponseDto> update(@PathVariable Long id, @Valid @RequestBody ProductVariantRequestDto dto) { return ResponseEntity.ok(service.update(id, dto)); }
    @DeleteMapping("/{id}") public ResponseEntity<Void> delete(@PathVariable Long id) { service.delete(id); return ResponseEntity.noContent().build(); }
}
