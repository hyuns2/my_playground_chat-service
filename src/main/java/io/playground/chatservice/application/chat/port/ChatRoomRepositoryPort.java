package io.playground.chatservice.application.chat.port;

import io.playground.chatservice.domain.chat.room.ChatRoom;

import java.time.LocalDateTime;

public interface ChatRoomRepositoryPort {
    Long save(ChatRoom chatRoom);

    void updateLastMessageAtByChatRoomId(LocalDateTime createdAt, Long chatRoomId);
}
