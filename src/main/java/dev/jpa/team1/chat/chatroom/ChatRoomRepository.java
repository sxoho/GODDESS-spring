package dev.jpa.team1.chat.chatroom;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ChatRoomRepository
        extends JpaRepository<ChatRoom, Long> {

    Optional<ChatRoom>
    findFirstByMemberIdAndStatusOrderByCreatedAtDesc(
            Long memberId,
            String status
    );

    List<ChatRoom>
    findByMemberIdOrderByCreatedAtDesc(
            Long memberId
    );
}