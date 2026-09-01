package dev.jpa.team1.chat.chatmessage;

import dev.jpa.team1.chat.chatroom.ChatRoomCreateRequestDto;
import dev.jpa.team1.chat.chatroom.ChatRoomResponseDto;
import dev.jpa.team1.chat.chatroom.ChatRoomService;
import dev.jpa.team1.common.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/chat/rooms")
@RequiredArgsConstructor
public class ChatController {

    private final ChatRoomService chatRoomService;
    private final ChatMessageService chatMessageService;

    // --- 채팅방 관리 (Room) ---

//    @PostMapping
//    public ResponseEntity<ApiResponse<ChatRoomResponseDto>> createChatRoom(
//            @AuthenticationPrincipal UserDetails userDetails,
//            @Valid @RequestBody ChatRoomCreateRequestDto requestDto) {
//
//        ChatRoomResponseDto response = chatRoomService.createChatRoom(userDetails.getUsername(), requestDto);
//        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
//    }

    // TODO: [JWT_BYPASS_TEMP]
    @PostMapping
    public ResponseEntity<ApiResponse<ChatRoomResponseDto>> createChatRoomWithNoAuth(
            @RequestParam(name = "loginId") String loginId,
            @Valid @RequestBody ChatRoomCreateRequestDto requestDto) {

        ChatRoomResponseDto response = chatRoomService.createChatRoom(loginId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

//    @GetMapping
//    public ResponseEntity<ApiResponse<List<ChatRoomResponseDto>>> getMyChatRooms(
//            @AuthenticationPrincipal UserDetails userDetails) {
//
//        List<ChatRoomResponseDto> response = chatRoomService.getMyChatRooms(userDetails.getUsername());
//        return ResponseEntity.ok(ApiResponse.success(response));
//    }

    // TODO: [JWT_BYPASS_TEMP]
    @GetMapping
    public ResponseEntity<ApiResponse<List<ChatRoomResponseDto>>> getMyChatRoomsWithNoAuth(
            @RequestParam(name = "loginId") String loginId) {

        List<ChatRoomResponseDto> response = chatRoomService.getMyChatRooms(loginId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }

//    @PatchMapping("/{roomId}/close")
//    public ResponseEntity<ApiResponse<Void>> closeChatRoom(
//            @AuthenticationPrincipal UserDetails userDetails,
//            @PathVariable Long roomId) {
//
//        chatRoomService.closeChatRoom(userDetails.getUsername(), roomId);
//        return ResponseEntity.ok(ApiResponse.success("채팅방이 종료되었습니다."));
//    }

    // TODO: [JWT_BYPASS_TEMP]
    @PatchMapping("/{roomId}/close")
    public ResponseEntity<ApiResponse<Void>> closeChatRoomWithNoAuth(
            @RequestParam(name = "loginId") String loginId,
            @PathVariable Long roomId) {

        chatRoomService.closeChatRoom(loginId, roomId);
        return ResponseEntity.ok(ApiResponse.success("채팅방이 종료되었습니다."));
    }

    // --- 채팅 메시지 관리 (Message) ---

//    @PostMapping("/{roomId}/messages")
//    public ResponseEntity<ApiResponse<ChatMessageResponseDto>> sendMessage(
//            @AuthenticationPrincipal UserDetails userDetails,
//            @PathVariable Long roomId,
//            @Valid @RequestBody ChatMessageSendRequestDto requestDto) {
//
//        ChatMessageResponseDto response = chatMessageService.sendMessage(userDetails.getUsername(), roomId, requestDto);
//        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
//    }

    // TODO: [JWT_BYPASS_TEMP]
    @PostMapping("/{roomId}/messages")
    public ResponseEntity<ApiResponse<ChatMessageResponseDto>> sendMessageWithNoAuth(
            @RequestParam(name = "loginId") String loginId,
            @PathVariable Long roomId,
            @Valid @RequestBody ChatMessageSendRequestDto requestDto) {

        ChatMessageResponseDto response = chatMessageService.sendMessage(loginId, roomId, requestDto);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(response));
    }

//    @GetMapping("/{roomId}/messages")
//    public ResponseEntity<ApiResponse<List<ChatMessageResponseDto>>> getMessages(
//            @AuthenticationPrincipal UserDetails userDetails,
//            @PathVariable Long roomId) {
//
//        List<ChatMessageResponseDto> response = chatMessageService.getMessages(userDetails.getUsername(), roomId);
//        return ResponseEntity.ok(ApiResponse.success(response));
//    }

    // TODO: [JWT_BYPASS_TEMP]
    @GetMapping("/{roomId}/messages")
    public ResponseEntity<ApiResponse<List<ChatMessageResponseDto>>> getMessagesWithNoAuth(
            @RequestParam(name = "loginId") String loginId,
            @PathVariable Long roomId) {

        List<ChatMessageResponseDto> response = chatMessageService.getMessages(loginId, roomId);
        return ResponseEntity.ok(ApiResponse.success(response));
    }
}