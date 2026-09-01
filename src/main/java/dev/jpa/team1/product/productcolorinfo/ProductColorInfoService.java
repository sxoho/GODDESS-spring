package dev.jpa.team1.product.productcolorinfo;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductColorInfoService {

    private final ProductColorInfoRepository repository;

    public List<ProductColorInfoResponseDto> findAll() {
        return repository.findAll().stream()
                .map(ProductColorInfoResponseDto::from)
                .toList();
    }

    public ProductColorInfoResponseDto findById(Long id) {
        ProductColorInfo info = repository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Color not found."));
        return ProductColorInfoResponseDto.from(info);
    }

}
