package dev.jpa.team1.chat.chatroom;

import dev.jpa.team1.commerce.orders.Orders;
import dev.jpa.team1.member.member.Member;
import dev.jpa.team1.product.product.Product;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Table(name = "chat_room")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ChatRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chat_room_seq")
    @SequenceGenerator(name = "chat_room_seq", sequenceName = "chat_room_seq", allocationSize = 1)
    @Column(name = "chat_room_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "counselor_member_id")
    private Member counselor;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Orders orders;

    @Column(name = "chat_type", nullable = false, length = 50)
    private String chatType;

    @Column(nullable = false, length = 50)
    private String status;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @Column(name = "closed_date")
    private LocalDateTime closedDate;

    public void closeRoom(String status, LocalDateTime now) {
        this.status = status;
        this.closedDate = now;
    }
}
