package dev.jpa.team1.fitting.quickfitting;

import dev.jpa.team1.member.member.Member;
import dev.jpa.team1.member.memberphoto.MemberPhoto;
import dev.jpa.team1.product.product.Product;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "quick_fitting")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class QuickFitting {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "quick_fitting_seq")
    @SequenceGenerator(name = "quick_fitting_seq", sequenceName = "quick_fitting_seq", allocationSize = 1)
    @Column(name = "quick_fitting_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_photo_id", nullable = false)
    private MemberPhoto memberPhoto;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    @Column(nullable = false, length = 20)
    private String status;

    @Lob
    @Column(name = "error_message")
    private String errorMessage;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "last_view_date")
    private LocalDateTime lastViewDate;

    public void updateStatus(String status, String errorMessage) {
        this.status = status;
        this.errorMessage = errorMessage;
    }
}
