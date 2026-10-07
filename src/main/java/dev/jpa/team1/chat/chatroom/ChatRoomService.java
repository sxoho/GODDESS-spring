package dev.jpa.team1.chat.chatroom;

import dev.jpa.team1.exception.CustomException;
import dev.jpa.team1.exception.ErrorCode;
import dev.jpa.team1.member.member.Member;
import dev.jpa.team1.member.member.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService {

    private final ChatRoomRepository chatRoomRepository;
    private final MemberRepository memberRepository;


    @Transactional
    public ChatRoomResponseDto
    getOrCreateActiveRoom(
            Long memberId
    ) {

        Member member =
                memberRepository
                        .findById(memberId)
                        .orElseThrow(
                                () ->
                                        new CustomException(
                                                ErrorCode.MEMBER_NOT_FOUND
                                        )
                        );


        return chatRoomRepository
                .findFirstByMemberIdAndStatusOrderByCreatedAtDesc(
                        memberId,
                        "ACTIVE"
                )
                .map(
                        ChatRoomResponseDto::from
                )
                .orElseGet(
                        () -> {

                            ChatRoom chatRoom =
                                    ChatRoom.builder()
                                            .member(member)
                                            .status("ACTIVE")
                                            .build();

                            return ChatRoomResponseDto.from(

                                    chatRoomRepository.save(
                                            chatRoom
                                    )
                            );
                        }
                );
    }


    @Transactional(readOnly = true)
    public List<ChatRoomResponseDto>
    getMemberChatRooms(
            Long memberId
    ) {

        memberRepository
                .findById(memberId)
                .orElseThrow(
                        () ->
                                new CustomException(
                                        ErrorCode.MEMBER_NOT_FOUND
                                )
                );


        return chatRoomRepository
                .findByMemberIdOrderByCreatedAtDesc(
                        memberId
                )
                .stream()
                .map(
                        ChatRoomResponseDto::from
                )
                .toList();
    }


    @Transactional
    public void closeChatRoom(

            Long memberId,

            Long roomId

    ) {

        ChatRoom chatRoom =
                getAccessibleRoom(
                        memberId,
                        roomId
                );


        if (
                !"CLOSED".equals(
                        chatRoom.getStatus()
                )
        ) {

            chatRoom.close();
        }
    }


    private ChatRoom getAccessibleRoom(

            Long memberId,

            Long roomId

    ) {

        ChatRoom chatRoom =
                chatRoomRepository
                        .findById(roomId)
                        .orElseThrow(
                                () ->
                                        new CustomException(
                                                ErrorCode.CHAT_ROOM_NOT_FOUND
                                        )
                        );


        if (
                !chatRoom
                        .getMember()
                        .getId()
                        .equals(memberId)
        ) {

            throw new CustomException(
                    ErrorCode.INVALID_CHAT_ACCESS
            );
        }


        return chatRoom;
    }
}