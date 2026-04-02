package io.playground.chatservice.application.port;

import io.playground.chatservice.domain.chat.ChatRoom;

import java.time.LocalDateTime;

public interface ChatRoomRepositoryPort {
    ChatRoom save(ChatRoom chatRoom);

    void updateLastMessageAtByChatRoomId(LocalDateTime createdAt, Long chatRoomId);
}
