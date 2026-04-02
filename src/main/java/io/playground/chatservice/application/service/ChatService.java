package io.playground.chatservice.application.service;

import io.playground.chatservice.application.command.CreateChatMessageCommand;
import io.playground.chatservice.application.command.CreateChatRoomCommand;
import io.playground.chatservice.application.command.GetChatMessagesCommand;
import io.playground.chatservice.application.command.SendChatMessageCommand;
import io.playground.chatservice.application.dto.ChatDto;
import io.playground.chatservice.application.port.*;
import io.playground.chatservice.common.exception.CustomErrorCode;
import io.playground.chatservice.common.exception.CustomException;
import io.playground.chatservice.domain.chat.ChatMessage;
import io.playground.chatservice.domain.chat.ChatParticipant;
import io.playground.chatservice.domain.chat.ChatRoom;
import io.playground.chatservice.domain.event.ChatMessageSentEvent;
import io.playground.chatservice.domain.userRead.UserReadModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatService implements ChatUsecase {
    private final UserReadModelRepositoryPort userReadModelRepositoryPort;
    private final ChatRoomRepositoryPort chatRoomRepositoryPort;
    private final ChatParticipantRepositoryPort chatParticipantRepositoryPort;
    private final ChatMessageRepositoryPort chatMessageRepositoryPort;
    private final EventPublisherPort eventPublisherPort;

    @Override
    @Transactional
    public ChatRoom createChatRoom(CreateChatRoomCommand command) {
        ChatRoom chatRoom = chatRoomRepositoryPort.save(
                ChatRoom.of(
                        null,
                        command.type(),
                        command.name(),
                        null
                )
        );

        List<ChatParticipant> chatParticipants = new ArrayList<>();
        if (!command.participantIds().contains(command.creatorId()))
            command.participantIds().add(command.creatorId());

        List<UserReadModel> userReadModels = userReadModelRepositoryPort
                .findAllByIds(command.participantIds());
        if (userReadModels.size() != command.participantIds().size())
            throw new CustomException(CustomErrorCode.PARTICIPANT_NOT_FOUND);

        userReadModels.forEach(userReadModel ->
            chatParticipants.add(
                    ChatParticipant.of(
                            null,
                            chatRoom.getId(),
                            userReadModel.getUserId(),
                            userReadModel.getNickName(),
                            userReadModel.getUserId().equals(command.creatorId()),
                            null
                    )
            )
        );
        chatParticipantRepositoryPort.saveAll(chatParticipants);
        eventPublisherPort.createStreamForChatRoom(chatRoom.getId());

        return chatRoom;
    }

    @Override
    @Transactional(readOnly = true)
    public List<ChatDto.GetChatRoomsResult> getChatRooms(String userId) {
        return chatParticipantRepositoryPort.findChatInfosByParticipantId(userId);
    }

    @Override
    @Transactional
    public ChatDto.GetChatMessagesResult getChatMessages(GetChatMessagesCommand command) {
        if (!chatParticipantRepositoryPort.existsByChatRoomIdAndParticipantId(
                command.chatRoomId(), command.userId()
        ))
            throw new CustomException(CustomErrorCode.INVALID_CHAT_PARTICIPANT);

        Map<String, Long> lastReadMessageIdInfos = chatParticipantRepositoryPort
                .findLastReadMessageIdInfosByChatRoomId(command.chatRoomId());
        List<ChatDto.ChatMessageInfoForDB> chatMessageInfoForDBS = chatMessageRepositoryPort
                .findAllByChatRoomIdOrderByCreatedAtDesc(command.chatRoomId());
        chatParticipantRepositoryPort.updateLastReadMessageIdByParticipantId(
                chatMessageInfoForDBS.get(0).getChatRoomId(),
                command.userId()
        );

        return ChatDto.GetChatMessagesResult.of(
                lastReadMessageIdInfos, chatMessageInfoForDBS);
    }

    @Override
    @Transactional
    public void sendChatMessage(SendChatMessageCommand command) {
        if (!chatParticipantRepositoryPort.existsByChatRoomIdAndParticipantId(
                command.chatRoomId(), command.senderId()
        ))
            throw new CustomException(CustomErrorCode.INVALID_CHAT_PARTICIPANT);

        if (command.parentMessageId() != null
                && !chatMessageRepositoryPort.existsByIdAndChatRoomId(
                        command.parentMessageId(), command.chatRoomId()))
            throw new CustomException(CustomErrorCode.INVALID_PARENT_MESSAGE);

        eventPublisherPort.publish(
                ChatMessageSentEvent.builder()
                        .chatRoomId(command.chatRoomId())
                        .senderId(command.senderId())
                        .type(command.type())
                        .content(command.content())
                        .parentMessageId(command.parentMessageId())
                        .createdAt(LocalDateTime.now())
                        .build()
        );
    }

    @Override
    @Transactional
    public void createChatMessage(CreateChatMessageCommand command) {
        ChatMessage chatMessage = chatMessageRepositoryPort.save(
                ChatMessage.of(
                        null,
                        command.chatRoomId(),
                        command.senderId(),
                        command.type(),
                        command.content(),
                        command.parentMessageId(),
                        command.createdAt()
                )
        );

        chatRoomRepositoryPort.updateLastMessageAtByChatRoomId(
                command.createdAt(),
                command.chatRoomId()
        );
    }
}
