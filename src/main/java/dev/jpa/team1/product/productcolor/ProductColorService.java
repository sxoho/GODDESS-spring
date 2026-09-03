package dev.jpa.team1.product.productcolor;
import dev.jpa.team1.product.product.*;
import dev.jpa.team1.product.productcolormaster.*;
import dev.jpa.team1.product.productcolorimage.*;
import dev.jpa.team1.product.productoption.ProductOptionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service @RequiredArgsConstructor
public class ProductColorService {
    private final ProductColorRepository repository;
    private final ProductRepository productRepository;
    private final ProductColorMasterRepository colorMasterRepository;
    private final ProductColorImageRepository imageRepository;
    private final ProductOptionRepository optionRepository;
    @Transactional(readOnly=true) public List<ProductColorResponseDto> findAll(){ return repository.findAll().stream().map(this::withImages).toList(); }
    @Transactional(readOnly=true) public List<ProductColorResponseDto> findByProductId(Long id){ return repository.findByProductIdOrderBySortOrderAsc(id).stream().map(this::withImages).toList(); }
    @Transactional(readOnly=true) public ProductColorResponseDto findById(Long id){ return withImages(repository.findById(id).orElseThrow()); }
    @Transactional public ProductColorResponseDto save(Long productId,ProductColorRequestDto d){
        Product p=productRepository.findById(productId).orElseThrow(); ProductColorMaster m=colorMasterRepository.findById(d.getColorId()).orElseThrow();
        if(repository.existsByProductIdAndColorMasterId(productId,d.getColorId())) throw new IllegalArgumentException("이미 등록된 상품 색상입니다.");
        return withImages(repository.save(ProductColor.builder().product(p).colorMaster(m).isMain(d.getIsMain()==null?"N":d.getIsMain()).sortOrder(d.getSortOrder()==null?1:d.getSortOrder()).aiApplyEnabled(d.getAiApplyEnabled()==null?"Y":d.getAiApplyEnabled()).visible(d.getVisible()==null?"Y":d.getVisible()).build()));
    }
    @Transactional public ProductColorResponseDto update(Long id,ProductColorRequestDto d){ ProductColor e=repository.findById(id).orElseThrow(); ProductColorMaster m=colorMasterRepository.findById(d.getColorId()).orElseThrow(); e.update(m,d.getIsMain(),d.getSortOrder(),d.getAiApplyEnabled(),d.getVisible()); return withImages(e); }
    private ProductColorResponseDto withImages(ProductColor c){ return ProductColorResponseDto.from(c,imageRepository.findByProductColorIdOrderBySortOrderAsc(c.getId()).stream().map(ProductColorImageResponseDto::from).toList()); }
    @Transactional public void delete(Long id){ optionRepository.deleteByProductColorId(id); imageRepository.deleteByProductColorId(id); repository.deleteById(id); }
}
