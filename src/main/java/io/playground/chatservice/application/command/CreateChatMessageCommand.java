package io.playground.chatservice.application.command;

import io.playground.chatservice.domain.chat.MessageType;
import lombok.Builder;

import java.time.LocalDateTime;

@Builder
public record CreateChatMessageCommand (
        Long chatRoomId,
        String senderId,
        MessageType type,
        String content,
        Long parentMessageId,
        LocalDateTime createdAt
){
}
