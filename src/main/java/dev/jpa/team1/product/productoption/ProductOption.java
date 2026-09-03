package dev.jpa.team1.product.productoption;
import dev.jpa.team1.exception.*;
import dev.jpa.team1.product.product.Product;
import dev.jpa.team1.product.productcolor.ProductColor;
import dev.jpa.team1.product.productvariant.ProductVariant;
import jakarta.persistence.*;
import lombok.*;
@Entity @Table(name="product_option") @Getter @NoArgsConstructor(access=AccessLevel.PROTECTED) @AllArgsConstructor @Builder
public class ProductOption {
    @Id @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="product_option_seq") @SequenceGenerator(name="product_option_seq",sequenceName="product_option_seq",allocationSize=1)
    @Column(name="productoption_id") private Long id;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="product_id",nullable=false) private Product product;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="productcolor_id") private ProductColor productColor;
    @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="productvariant_id") private ProductVariant productVariant;
    @Builder.Default @Column(name="add_price",nullable=false) private Integer addPrice=0;
    @Builder.Default @Column(nullable=false) private Integer stock=0;
    @Builder.Default @Column(nullable=false,length=1) private String visible="Y";
    public void update(ProductColor color,ProductVariant variant,Integer addPrice,Integer stock,String visible){this.productColor=color;this.productVariant=variant;this.addPrice=addPrice==null?0:addPrice;this.stock=stock==null?0:stock;this.visible=visible==null?"Y":visible;}
    public void decreaseStock(Integer quantity){ if(this.stock<quantity) throw new CustomException(ErrorCode.OUT_OF_STOCK); this.stock-=quantity; }
}
