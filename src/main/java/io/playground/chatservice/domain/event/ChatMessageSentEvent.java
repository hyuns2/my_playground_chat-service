package io.playground.chatservice.domain.event;

import io.playground.chatservice.domain.chat.MessageType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record ChatMessageSentEvent(
        Long chatRoomId,
        String senderId,
        MessageType type,
        String content,
        Long parentMessageId,
        LocalDateTime createdAt
) {
}
