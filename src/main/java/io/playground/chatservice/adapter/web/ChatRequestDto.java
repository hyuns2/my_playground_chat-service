package io.playground.chatservice.adapter.web;

import io.playground.chatservice.domain.chat.MessageType;
import io.playground.chatservice.domain.chat.RoomType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class ChatRequestDto {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class CreateChatRoom {
        @NotNull
        RoomType type;

        String name;

        @NotEmpty
        List<String> participantIds;
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class SendChatMessage {
        @NotBlank
        String senderId;

        @NotNull
        Long chatRoomId;

        @NotNull
        MessageType type;

        String content;

        Long parentMessageId;
    }
}
