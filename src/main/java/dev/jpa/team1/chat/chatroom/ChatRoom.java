package dev.jpa.team1.chat.chatroom;

import dev.jpa.team1.member.member.Member;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "CHAT_ROOM")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ChatRoom {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "chat_room_seq"
    )
    @SequenceGenerator(
            name = "chat_room_seq",
            sequenceName = "CHAT_ROOM_SEQ",
            allocationSize = 1
    )
    @Column(name = "CHAT_ROOM_ID")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "MEMBER_ID",
            nullable = false
    )
    private Member member;


    @Builder.Default
    @Column(
            name = "STATUS",
            nullable = false,
            length = 20
    )
    private String status = "ACTIVE";


    @Column(
            name = "CREATED_AT",
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;


    @Column(
            name = "UPDATED_AT",
            nullable = false
    )
    private LocalDateTime updatedAt;


    @Column(name = "CLOSED_AT")
    private LocalDateTime closedAt;


    /*
     * 최초 생성
     */
    @PrePersist
    protected void onCreate() {

        LocalDateTime now =
                LocalDateTime.now();

        if (createdAt == null) {

            createdAt = now;
        }

        if (updatedAt == null) {

            updatedAt = now;
        }
    }


    /*
     * 수정 시 UPDATED_AT 갱신
     */
    @PreUpdate
    protected void onUpdate() {

        updatedAt =
                LocalDateTime.now();
    }


    /*
     * 채팅방 종료
     */
    public void close() {

        this.status =
                "CLOSED";

        this.closedAt =
                LocalDateTime.now();
    }
}