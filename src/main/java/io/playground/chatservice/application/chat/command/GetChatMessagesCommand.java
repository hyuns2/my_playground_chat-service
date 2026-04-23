package io.playground.chatservice.application.chat.command;

public record GetChatMessagesCommand(
        String userId,
        Long chatRoomId,
        int page,
        int size
) {
    public static GetChatMessagesCommand of(String userId,
                                            Long chatRoomId,
                                            int page,
                                            int size) {
        return new GetChatMessagesCommand(
                userId,
                chatRoomId,
                page,
                size
        );
    }
}
