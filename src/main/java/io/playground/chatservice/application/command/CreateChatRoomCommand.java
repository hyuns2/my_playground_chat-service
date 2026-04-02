package io.playground.chatservice.application.command;

import io.playground.chatservice.domain.chat.RoomType;
import lombok.Builder;

import java.util.List;

@Builder
public record CreateChatRoomCommand(
        String creatorId,
        RoomType type,
        String name,
        List<String> participantIds
) {
}
