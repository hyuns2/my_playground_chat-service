package io.playground.chatservice.application.chat.command;

import io.playground.chatservice.domain.chat.message.ChatMessage.MessageType;
import io.playground.chatservice.domain.chat.message.ChatMessageSentEvent;

import java.time.LocalDateTime;

public record SaveChatMessageCommand(
        Long chatRoomId,
        String senderId,
        MessageType type,
        String content,
        Long parentMessageId,
        LocalDateTime createdAt
) {
    public static SaveChatMessageCommand from(ChatMessageSentEvent event) {
        return new SaveChatMessageCommand(
                event.chatRoomId(),
                event.senderId(),
                event.type(),
                event.content(),
                event.parentMessageId(),
                event.createdAt()
        );
    }
}
