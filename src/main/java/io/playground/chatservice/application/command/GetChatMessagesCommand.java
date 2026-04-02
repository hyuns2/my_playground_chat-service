package io.playground.chatservice.application.command;

import lombok.Builder;

@Builder
public record GetChatMessagesCommand(
        String userId,
        Long chatRoomId
) {
}
