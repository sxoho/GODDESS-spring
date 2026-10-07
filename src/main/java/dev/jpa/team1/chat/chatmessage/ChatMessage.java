package dev.jpa.team1.chat.chatmessage;

import dev.jpa.team1.chat.chatroom.ChatRoom;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "CHAT_MESSAGE")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ChatMessage {

    @Id
    @GeneratedValue(
            strategy = GenerationType.SEQUENCE,
            generator = "chat_message_seq"
    )
    @SequenceGenerator(
            name = "chat_message_seq",
            sequenceName = "CHAT_MESSAGE_SEQ",
            allocationSize = 1
    )
    @Column(name = "CHAT_MESSAGE_ID")
    private Long id;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(
            name = "CHAT_ROOM_ID",
            nullable = false
    )
    private ChatRoom chatRoom;


    @Column(
            name = "SENDER_TYPE",
            nullable = false,
            length = 20
    )
    private String senderType;


    @Lob
    @Column(
            name = "MESSAGE",
            nullable = false
    )
    private String message;


    @Column(
            name = "CREATED_AT",
            nullable = false,
            updatable = false
    )
    private LocalDateTime createdAt;


    /*
     * DB INSERT 직전에
     * 생성시간을 Java 객체에도 직접 넣는다.
     *
     * 따라서 저장 직후 DTO로 변환해도
     * createdAt이 null이 되지 않는다.
     */
    @PrePersist
    protected void onCreate() {

        if (createdAt == null) {

            createdAt =
                    LocalDateTime.now();
        }
    }
}