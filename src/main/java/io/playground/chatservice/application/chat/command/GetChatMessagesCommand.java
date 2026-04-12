package io.playground.chatservice.application.chat.command;

public record GetChatMessagesCommand(
        String userId,
        Long chatRoomId
) {
    public static GetChatMessagesCommand of(String userId, Long chatRoomId) {
        return new GetChatMessagesCommand(userId, chatRoomId);
    }
}
