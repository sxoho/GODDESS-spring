package dev.jpa.team1.product.product;

import dev.jpa.team1.product.category.Category;
import dev.jpa.team1.product.category.CategoryRepository;
import dev.jpa.team1.product.search.ProductSearchKeyword;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.*;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import jakarta.persistence.criteria.*;
import java.util.*;

@Service @RequiredArgsConstructor
public class ProductService {
    private final ProductRepository repository;
    private final CategoryRepository categoryRepository;

    @Transactional(readOnly=true) public List<ProductResponseDto> findAll(){ return repository.findAll().stream().map(ProductResponseDto::from).toList(); }

    @Transactional(readOnly=true)
    public Page<ProductResponseDto> findPage(int page,int size,Long mainCategoryId,Long categoryId,String sort,String keyword){
        return repository.findAll(buildSearchSpecification(mainCategoryId,categoryId,terms(keyword),true), PageRequest.of(page,size,getSort(sort))).map(ProductResponseDto::from);
    }

    @Transactional(readOnly=true)
    public Page<ProductResponseDto> findAdminPage(int page,int size,Long mainCategoryId,Long categoryId,String sort,String keyword){
        return repository.findAll(buildSearchSpecification(mainCategoryId,categoryId,terms(keyword),false), PageRequest.of(page,size,getSort(sort))).map(ProductResponseDto::from);
    }

    @Transactional(readOnly=true)
    public List<ProductResponseDto> findBest100(String keyword){
        return repository.findAll(buildSearchSpecification(null,null,terms(keyword),true), PageRequest.of(0,100,Sort.by(Sort.Direction.DESC,"viewCount")))
                .getContent().stream().map(ProductResponseDto::from).toList();
    }

    private List<String> terms(String keyword){
        String k=keyword==null?"":keyword.trim();
        return k.isBlank()?List.of(): ProductSearchKeyword.buildSearchTerms(k);
    }

    private Specification<Product> buildSearchSpecification(Long mainCategoryId,Long categoryId,List<String> searchTerms,boolean publicOnly){
        return (root,query,cb)->{
            Join<Product,Category> category=root.join("category");
            Join<Category,Category> parent=category.join("parent", JoinType.LEFT);
            List<Predicate> c=new ArrayList<>();
            if(publicOnly) c.add(cb.equal(root.get("status"),"ON_SALE"));
            if(categoryId!=null) c.add(cb.equal(category.get("id"),categoryId));
            if(mainCategoryId!=null) c.add(cb.or(cb.equal(category.get("id"),mainCategoryId),cb.equal(parent.get("id"),mainCategoryId)));
            if(!searchTerms.isEmpty()){
                List<Predicate> keys=new ArrayList<>();
                for(String term:searchTerms){
                    String p="%"+term.toLowerCase()+"%";
                    keys.add(cb.like(cb.lower(root.get("name")),p));
                    keys.add(cb.like(cb.lower(root.get("brand")),p));
                    keys.add(cb.like(cb.lower(category.get("name")),p));
                    keys.add(cb.like(cb.lower(parent.get("name")),p));
                }
                c.add(cb.or(keys.toArray(new Predicate[0])));
            }
            return cb.and(c.toArray(new Predicate[0]));
        };
    }

    private Sort getSort(String sort){
        if("priceLow".equals(sort)) return Sort.by(Sort.Direction.ASC,"price");
        if("priceHigh".equals(sort)) return Sort.by(Sort.Direction.DESC,"price");
        return Sort.by(Sort.Direction.DESC,"createdAt");
    }

    @Transactional(readOnly=true) public ProductResponseDto findById(Long id){ return ProductResponseDto.from(repository.findById(id).orElseThrow(()->new IllegalArgumentException("Product not found. id="+id))); }

    @Transactional public ProductResponseDto save(ProductRequestDto d){
        Category category=categoryRepository.findById(d.getCategoryId()).orElseThrow(()->new IllegalArgumentException("Category not found. id="+d.getCategoryId()));
        Product p=Product.builder().category(category).name(d.getName()).description(d.getDescription()).price(d.getPrice())
                .discount(d.getDiscount()==null?0:d.getDiscount()).brand(d.getBrand()).usageInfo(d.getUsageInfo())
                .aiApplyEnabled(d.getAiApplyEnabled()==null?"Y":d.getAiApplyEnabled()).productType(d.getProductType()).volume(d.getVolume())
                .skinType(d.getSkinType()).personalColor(d.getPersonalColor()).status(d.getStatus()==null?"ON_SALE":d.getStatus()).build();
        return ProductResponseDto.from(repository.save(p));
    }

    @Transactional public ProductResponseDto update(Long id,ProductRequestDto d){
        Product p=repository.findById(id).orElseThrow(()->new IllegalArgumentException("Product not found. id="+id));
        Category c=categoryRepository.findById(d.getCategoryId()).orElseThrow(()->new IllegalArgumentException("Category not found. id="+d.getCategoryId()));
        p.update(c,d.getName(),d.getDescription(),d.getPrice(),d.getDiscount(),d.getBrand(),d.getUsageInfo(),d.getAiApplyEnabled(),d.getProductType(),d.getVolume(),d.getSkinType(),d.getPersonalColor(),d.getStatus());
        return ProductResponseDto.from(p);
    }
    @Transactional public void stopSale(Long id){ repository.findById(id).orElseThrow().stopSale(); }
    @Transactional public void resumeSale(Long id){ repository.findById(id).orElseThrow().resumeSale(); }
}
