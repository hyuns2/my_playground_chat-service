package io.playground.chatservice.application.chat.dto;

import io.playground.chatservice.application.chat.command.SendChatMessageCommand;
import io.playground.chatservice.domain.chat.message.ChatMessage;
import io.playground.chatservice.domain.chat.message.ChatMessageSentEvent;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
public class ChatMessageInfoDto {
    Long chatRoomId;

    String senderId;

    ChatMessage.MessageType type;

    String content;

    Long parentMessageId;

    LocalDateTime createdAt;

    public static ChatMessageInfoDto from(ChatMessageSentEvent event) {
        return new ChatMessageInfoDto(
                event.chatRoomId(),
                event.senderId(),
                event.type(),
                event.content(),
                event.parentMessageId(),
                event.createdAt()
        );
    }

    public static ChatMessageInfoDto from(SendChatMessageCommand command) {
        return new ChatMessageInfoDto(
                command.chatRoomId(),
                command.senderId(),
                command.type(),
                command.content(),
                command.parentMessageId(),
                command.createdAt()
        );
    }
}
