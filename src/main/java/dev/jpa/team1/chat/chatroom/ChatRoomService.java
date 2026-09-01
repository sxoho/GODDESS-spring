package dev.jpa.team1.chat.chatroom;

import dev.jpa.team1.commerce.orders.Orders;
import dev.jpa.team1.commerce.orders.OrdersRepository;
import dev.jpa.team1.exception.CustomException;
import dev.jpa.team1.exception.ErrorCode;
import dev.jpa.team1.member.member.Member;
import dev.jpa.team1.member.member.MemberRepository;
import dev.jpa.team1.product.product.Product;
import dev.jpa.team1.product.product.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;
    private final ProductRepository productRepository;
    private final OrdersRepository ordersRepository;

    @Transactional
    public ChatRoomResponseDto createChatRoom(String loginId, ChatRoomCreateRequestDto requestDto) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        Product product = null;
        if (requestDto.getProductId() != null) {
            product = productRepository.findById(requestDto.getProductId())
                    .orElseThrow(() -> new CustomException(ErrorCode.PRODUCT_NOT_FOUND));
        }

        Orders order = null;
        if (requestDto.getOrderId() != null) {
            order = ordersRepository.findById(requestDto.getOrderId())
                    .orElseThrow(() -> new CustomException(ErrorCode.ORDER_NOT_FOUND));
        }

        ChatRoom chatRoom = ChatRoom.builder()
                .member(member)
                .product(product)
                .orders(order)
                .chatType(requestDto.getChatType())
                .status("ACTIVE") // 초기 상태 활성화
                .build();

        ChatRoom savedRoom = chatRoomRepository.save(chatRoom);
        return ChatRoomResponseDto.from(savedRoom);
    }

    @Transactional(readOnly = true)
    public List<ChatRoomResponseDto> getMyChatRooms(String loginId) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        return chatRoomRepository.findByMemberIdOrderByIdDesc(member.getId()).stream()
                .map(ChatRoomResponseDto::from)
                .collect(Collectors.toList());
    }

    @Transactional
    public void closeChatRoom(String loginId, Long roomId) {
        Member member = memberRepository.findByLoginId(loginId)
                .orElseThrow(() -> new CustomException(ErrorCode.MEMBER_NOT_FOUND));

        ChatRoom chatRoom = chatRoomRepository.findById(roomId)
                .orElseThrow(() -> new CustomException(ErrorCode.CHAT_ROOM_NOT_FOUND));

        if (!chatRoom.getMember().getId().equals(member.getId())) {
            throw new CustomException(ErrorCode.INVALID_CHAT_ACCESS);
        }

        // 엔티티 내부 상태 업데이트 메서드 호출
        chatRoom.closeRoom("CLOSED", LocalDateTime.now());
    }
}