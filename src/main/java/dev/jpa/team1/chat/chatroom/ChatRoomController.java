package dev.jpa.team1.chat.chatroom;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat/rooms")
@RequiredArgsConstructor
public class ChatRoomController {

    private final ChatRoomService chatRoomService;


    /*
     * =========================================
     * ACTIVE 채팅방 조회 또는 생성
     * =========================================
     */
    @PostMapping
    public ResponseEntity<ChatRoomResponseDto>
    getOrCreateRoom(

            @RequestParam("memberId")
            Long memberId

    ) {

        return ResponseEntity.ok(

                chatRoomService
                        .getOrCreateActiveRoom(
                                memberId
                        )
        );
    }


    /*
     * =========================================
     * 회원의 채팅방 목록 조회
     * =========================================
     */
    @GetMapping("/member/{memberId}")
    public ResponseEntity<List<ChatRoomResponseDto>>
    getMemberRooms(

            @PathVariable("memberId")
            Long memberId

    ) {

        return ResponseEntity.ok(

                chatRoomService
                        .getMemberChatRooms(
                                memberId
                        )
        );
    }


    /*
     * =========================================
     * 채팅방 종료
     * =========================================
     */
    @PatchMapping("/{roomId}/close")
    public ResponseEntity<Void>
    closeRoom(

            @PathVariable("roomId")
            Long roomId,

            @RequestParam("memberId")
            Long memberId

    ) {

        chatRoomService.closeChatRoom(
                memberId,
                roomId
        );

        return ResponseEntity.ok().build();
    }
}