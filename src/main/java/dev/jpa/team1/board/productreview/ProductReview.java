package dev.jpa.team1.board.productreview;
import dev.jpa.team1.beauty.save.BeautySave;
import dev.jpa.team1.commerce.orderdetail.OrderDetail;
import dev.jpa.team1.member.member.Member;
import dev.jpa.team1.product.product.Product;
import jakarta.persistence.*; import lombok.*; import org.hibernate.annotations.CreationTimestamp; import org.hibernate.annotations.UpdateTimestamp; import java.time.LocalDateTime;
@Entity @Table(name="product_review") @Getter @NoArgsConstructor(access=AccessLevel.PROTECTED) @AllArgsConstructor @Builder
public class ProductReview {
 @Id @GeneratedValue(strategy=GenerationType.SEQUENCE,generator="product_review_seq") @SequenceGenerator(name="product_review_seq",sequenceName="product_review_seq",allocationSize=1) @Column(name="review_id") private Long id;
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="member_id",nullable=false) private Member member;
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="orderdetail_id",nullable=false) private OrderDetail orderDetail;
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="product_id",nullable=false) private Product product;
 @ManyToOne(fetch=FetchType.LAZY) @JoinColumn(name="beautysave_id") private BeautySave beautySave;
 @Column(nullable=false,length=200) private String title; @Lob @Column(nullable=false) private String content; @Column(nullable=false) private Integer rating;
 @CreationTimestamp @Column(name="created_at",updatable=false) private LocalDateTime createdAt; @UpdateTimestamp @Column(name="updated_at") private LocalDateTime updatedAt; @Builder.Default @Column(nullable=false,length=1) private String visible="Y";
 public void update(String title,String content,Integer rating,BeautySave beautySave){this.title=title;this.content=content;this.rating=rating;this.beautySave=beautySave;} public void changeVisible(String visible){this.visible=visible;}
}
