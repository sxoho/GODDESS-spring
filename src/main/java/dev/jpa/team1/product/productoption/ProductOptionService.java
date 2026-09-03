package dev.jpa.team1.product.productoption;
import dev.jpa.team1.product.product.*;
import dev.jpa.team1.product.productcolor.*;
import dev.jpa.team1.product.productvariant.*;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
@Service @RequiredArgsConstructor
public class ProductOptionService {
 private final ProductOptionRepository repository; private final ProductRepository productRepository; private final ProductColorRepository colorRepository; private final ProductVariantRepository variantRepository;
 @Transactional(readOnly=true) public List<ProductOptionResponseDto> findAll(){return repository.findAll().stream().map(ProductOptionResponseDto::from).toList();}
 @Transactional(readOnly=true) public List<ProductOptionResponseDto> findByProductId(Long id){return repository.findByProductIdOrderByIdAsc(id).stream().map(ProductOptionResponseDto::from).toList();}
 @Transactional(readOnly=true) public List<ProductOptionResponseDto> findAvailableByProductId(Long id){return repository.findByProductIdAndVisibleAndStockGreaterThanOrderByIdAsc(id,"Y",0).stream().map(ProductOptionResponseDto::from).toList();}
 @Transactional(readOnly=true) public ProductOptionResponseDto findById(Long id){return ProductOptionResponseDto.from(repository.findById(id).orElseThrow());}
 private ProductColor color(Long id,Long productId){ if(id==null)return null; ProductColor c=colorRepository.findById(id).orElseThrow(); if(!c.getProduct().getId().equals(productId))throw new IllegalArgumentException("해당 상품의 색상이 아닙니다."); return c; }
 private ProductVariant variant(Long id,Long productId){ if(id==null)return null; ProductVariant v=variantRepository.findById(id).orElseThrow(); if(!v.getProduct().getId().equals(productId))throw new IllegalArgumentException("해당 상품의 옵션이 아닙니다."); return v; }
 @Transactional public ProductOptionResponseDto save(Long productId,ProductOptionRequestDto d){ Product p=productRepository.findById(productId).orElseThrow(); ProductColor c=color(d.getProductColorId(),productId); ProductVariant v=variant(d.getProductVariantId(),productId); if(repository.existsByProductIdAndProductColorIdAndProductVariantId(productId,d.getProductColorId(),d.getProductVariantId()))throw new IllegalArgumentException("이미 등록된 상품 옵션입니다."); return ProductOptionResponseDto.from(repository.save(ProductOption.builder().product(p).productColor(c).productVariant(v).addPrice(d.getAddPrice()==null?0:d.getAddPrice()).stock(d.getStock()==null?0:d.getStock()).visible(d.getVisible()==null?"Y":d.getVisible()).build())); }
 @Transactional public ProductOptionResponseDto update(Long id,ProductOptionRequestDto d){ProductOption e=repository.findById(id).orElseThrow();Long pid=e.getProduct().getId();e.update(color(d.getProductColorId(),pid),variant(d.getProductVariantId(),pid),d.getAddPrice(),d.getStock(),d.getVisible());return ProductOptionResponseDto.from(e);}
 @Transactional public void delete(Long id){repository.deleteById(id);}
}
