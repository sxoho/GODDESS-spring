package dev.jpa.team1.product.productcolorinfo;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/product_color_info")
@RequiredArgsConstructor
public class ProductColorInfoController {

    private final ProductColorInfoService service;

    @GetMapping("/find_all")
    public List<ProductColorInfoResponseDto> findAll() {
        return service.findAll();
    }

    @GetMapping("/{id}")
    public ProductColorInfoResponseDto findById(@PathVariable Long id) {
        return service.findById(id);
    }

}
