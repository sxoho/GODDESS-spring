package dev.jpa.team1.product.productcolormaster;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/product_color_master")
@RequiredArgsConstructor
public class ProductColorMasterController {
    private final ProductColorMasterRepository repository;

    @GetMapping("/find_all")
    public List<ProductColorMasterDto> all() {
        return repository.findAll().stream().map(ProductColorMasterDto::from).toList();
    }

    @PostMapping("/save")
    public ProductColorMasterDto save(@RequestBody ProductColorMasterDto d) {
        return ProductColorMasterDto.from(
                repository.save(
                        ProductColorMaster.builder()
                                .colorName(d.getColorName())
                                .colorCode(d.getColorCode())
                                .baseColorGroup(d.getBaseColorGroup())
                                .build()
                )
        );
    }

    @PutMapping("/{id}")
    public ProductColorMasterDto update(
            @PathVariable(name = "id") Long id,
            @RequestBody ProductColorMasterDto d
    ) {
        ProductColorMaster e = repository.findById(id).orElseThrow();
        e.update(d.getColorName(), d.getColorCode(), d.getBaseColorGroup());
        return ProductColorMasterDto.from(repository.save(e));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(name = "id") Long id) {
        repository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
