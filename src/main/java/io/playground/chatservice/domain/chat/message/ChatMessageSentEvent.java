package io.playground.chatservice.domain.chat.message;

import io.playground.chatservice.domain.chat.message.ChatMessage.MessageType;

import java.time.LocalDateTime;

public record ChatMessageSentEvent(
        Long chatRoomId,
        String senderId,
        MessageType type,
        String content,
        Long parentMessageId,
        LocalDateTime createdAt
) {
    public static ChatMessageSentEvent of(Long chatRoomId,
                                          String senderId,
                                          MessageType type,
                                          String content,
                                          Long parentMessageId,
                                          LocalDateTime createdAt) {
        return new ChatMessageSentEvent(
                chatRoomId,
                senderId,
                type,
                content,
                parentMessageId,
                createdAt
        );
    }
}
