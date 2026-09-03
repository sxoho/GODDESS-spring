package dev.jpa.team1.chat.chatmessage;

import dev.jpa.team1.chat.chatroom.ChatRoom;
import dev.jpa.team1.member.member.Member;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;


@Entity
@Table(name = "chat_message")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ChatMessage {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "chat_message_seq")
    @SequenceGenerator(name = "chat_message_seq", sequenceName = "chat_message_seq", allocationSize = 1)
    @Column(name = "chatmessage_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chatroom_id", nullable = false)
    private ChatRoom chatRoom;

    @Column(name = "sender_type", nullable = false, length = 50)
    private String senderType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sender_member_id")
    private Member sender;

    @Lob
    @Column(nullable = false)
    private String message;

    @Column(name = "message_type", nullable = false, length = 50)
    private String messageType;

    @Column(name = "is_read", nullable = false, length = 1)
    private String isRead;

    @CreationTimestamp
    @Column(updatable = false)
    private LocalDateTime createdAt;
}
