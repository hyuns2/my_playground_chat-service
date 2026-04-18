package io.playground.chatservice.application.chat.service;

import io.playground.chatservice.application.chat.command.CreateChatRoomCommand;
import io.playground.chatservice.application.chat.port.ChatParticipantRepositoryPort;
import io.playground.chatservice.application.chat.port.ChatRoomRepositoryPort;
import io.playground.chatservice.application.chat.port.EventPublisherPort;
import io.playground.chatservice.application.eventstream.PubEventType;
import io.playground.chatservice.application.read.model.UserView;
import io.playground.chatservice.application.read.port.UserViewQueryPort;
import io.playground.chatservice.domain.chat.ChatParticipant;
import io.playground.chatservice.domain.chat.message.ChatMessage;
import io.playground.chatservice.domain.chat.message.ChatMessageSentEvent;
import io.playground.chatservice.domain.chat.room.ChatRoom;
import io.playground.chatservice.exception.CustomErrorCode;
import io.playground.chatservice.exception.CustomException;
import io.playground.chatservice.infrastructure.persistence.chat.dto.ChatQueryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ChatRoomService implements ChatRoomUsecase {
    private final UserViewQueryPort userViewQueryPort;
    private final ChatRoomRepositoryPort chatRoomRepositoryPort;
    private final ChatParticipantRepositoryPort chatParticipantRepositoryPort;
    private final EventPublisherPort eventPublisherPort;

    @Override
    @Transactional
    public Long createChatRoom(CreateChatRoomCommand command) {
        if (!command.participantIds().contains(command.creatorId()))
            command.participantIds().add(command.creatorId());

        List<UserView> userViews = userViewQueryPort
                .findAllByIds(command.participantIds());
        if (userViews.size() != command.participantIds().size())
            throw new CustomException(CustomErrorCode.PARTICIPANT_NOT_FOUND);

        ChatRoom chatRoom = ChatRoom.of(
                null,
                command.type(),
                command.name(),
                null
        );
        Long chatRoomId = chatRoomRepositoryPort.save(chatRoom);

        List<ChatParticipant> chatParticipants = new ArrayList<>();
        for (UserView userView : userViews) {
            chatParticipants.add(
                    ChatParticipant.of(
                            null,
                            chatRoomId,
                            userView.getUserId(),
                            userView.getNickName(),
                            userView.getUserId().equals(command.creatorId()),
                            null
                    )
            );
        }
        chatParticipantRepositoryPort.saveAll(chatParticipants);

        // Todo: 추후 채팅방 생성시점도 request에서 받도록 변경
        eventPublisherPort.publish(
                PubEventType.CHAT_MESSAGE_SENT,
                chatRoomId.toString(),
                ChatMessageSentEvent.of(
                        chatRoomId,
                        command.creatorId(),
                        ChatMessage.MessageType.IN,
                        "채팅방이 생성되었습니다.",
                        null,
                        LocalDateTime.now()
                )
        );

        return chatRoomId;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatQueryDto.ChatRoomInfo> getChatRooms(String userId) {
        return chatParticipantRepositoryPort.findChatInfosByParticipantId(userId);
    }

    @Override
    @Transactional(readOnly = true)
    public List<Long> getChatRoomIds(String userId) {
        return chatParticipantRepositoryPort.findChatRoomIdsByParticipantId(userId);
    }
}
