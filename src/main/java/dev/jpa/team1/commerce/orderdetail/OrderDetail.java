package dev.jpa.team1.commerce.orderdetail;
import dev.jpa.team1.commerce.orders.Orders;
import dev.jpa.team1.product.productoption.ProductOption;
import jakarta.persistence.*;
import lombok.*;
@Entity @Table(name="order_detail") @Getter @NoArgsConstructor(access=AccessLevel.PROTECTED) @AllArgsConstructor @Builder
public class OrderDetail {
 @Id @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="order_detail_seq") @SequenceGenerator(name="order_detail_seq",sequenceName="order_detail_seq",allocationSize=1) @Column(name="orderdetail_id") private Long id;
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="order_id",nullable=false) private Orders orders;
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="productoption_id",nullable=false) private ProductOption productOption;
 @Column(name="product_name",nullable=false,length=200) private String productName;
 @Column(name="color_name",length=50) private String colorName;
 @Column(name="variant_name",length=50) private String variantName;
 @Column(nullable=false) private Integer price;
 @Builder.Default @Column(nullable=false) private Integer quantity=1;
}
