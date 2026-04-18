package io.playground.chatservice.application.chat.service;

import io.playground.chatservice.application.chat.command.GetChatMessagesCommand;
import io.playground.chatservice.application.chat.command.SendChatMessageCommand;
import io.playground.chatservice.application.chat.port.ChatMessageRepositoryPort;
import io.playground.chatservice.application.chat.port.ChatParticipantRepositoryPort;
import io.playground.chatservice.application.chat.port.ChatRoomRepositoryPort;
import io.playground.chatservice.application.chat.port.EventPublisherPort;
import io.playground.chatservice.application.eventstream.PubEventType;
import io.playground.chatservice.domain.chat.message.ChatMessage;
import io.playground.chatservice.domain.chat.message.ChatMessageSentEvent;
import io.playground.chatservice.exception.CustomErrorCode;
import io.playground.chatservice.exception.CustomException;
import io.playground.chatservice.infrastructure.persistence.chat.dto.ChatQueryDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatMessageService implements ChatMessageUsecase {
    private final ChatRoomRepositoryPort chatRoomRepositoryPort;
    private final ChatParticipantRepositoryPort chatParticipantRepositoryPort;
    private final ChatMessageRepositoryPort chatMessageRepositoryPort;
    private final EventPublisherPort eventPublisherPort;

    @Override
    @Transactional
    public ChatQueryDto.ChatMessagesInfo getChatMessages(GetChatMessagesCommand command) {
        if (!chatParticipantRepositoryPort.existsByChatRoomIdAndParticipantId(
                command.chatRoomId(), command.userId()
        ))
            throw new CustomException(CustomErrorCode.INVALID_CHAT_PARTICIPANT);

        Map<String, Long> lastReadMessageIdInfos = chatParticipantRepositoryPort
                .findLastReadMessageIdInfosByChatRoomId(command.chatRoomId());
        List<ChatQueryDto.ChatMessage> chatMessages = chatMessageRepositoryPort
                .findAllByChatRoomIdOrderByCreatedAtDesc(command.chatRoomId());

        if (!chatMessages.isEmpty())
            chatParticipantRepositoryPort.updateLastReadMessageIdByParticipantId(
                    chatMessages.get(0).getChatMessageId(),
                    command.userId()
            );

        return ChatQueryDto.ChatMessagesInfo.of(
                lastReadMessageIdInfos, chatMessages);
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

        saveChatMessage(command);

        eventPublisherPort.publish(
                PubEventType.CHAT_MESSAGE_SENT,
                command.chatRoomId().toString(),
                ChatMessageSentEvent.of(
                        command.chatRoomId(),
                        command.senderId(),
                        command.type(),
                        command.content(),
                        command.parentMessageId(),
                        LocalDateTime.now()
                )
        );
    }

    private void saveChatMessage(SendChatMessageCommand command) {
        chatMessageRepositoryPort.save(
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
