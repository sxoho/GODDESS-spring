package dev.jpa.team1.commerce.cart;

import dev.jpa.team1.fitting.coordfittingresult.CoordFittingResult;
import dev.jpa.team1.fitting.quickfittingresult.QuickFittingResult;
import dev.jpa.team1.member.member.Member;
import dev.jpa.team1.product.productoption.ProductOption;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "cart")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class Cart {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "cart_seq")
    @SequenceGenerator(name = "cart_seq", sequenceName = "cart_seq", allocationSize = 1)
    @Column(name = "cart_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_option_id", nullable = false)
    private ProductOption productOption;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "quick_fitting_result_id")
    private QuickFittingResult quickFittingResult;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "coord_fitting_result_id")
    private CoordFittingResult coordFittingResult;

    @Column(name = "file_path", length = 500)
    private String filePath;

    @Column(nullable = false)
    private Integer quantity;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
