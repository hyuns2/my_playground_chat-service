package io.playground.chatservice.infrastructure.persistence.chat.adapter;

import io.playground.chatservice.application.chat.port.ChatRoomRepositoryPort;
import io.playground.chatservice.domain.chat.room.ChatRoom;
import io.playground.chatservice.infrastructure.persistence.chat.entity.ChatRoomJpaEntity;
import io.playground.chatservice.infrastructure.persistence.chat.repository.ChatRoomJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ChatRoomPersistenceAdapter implements ChatRoomRepositoryPort {
    private final ChatRoomJpaRepository chatRoomRepository;

    @Override
    public Long save(ChatRoom chatRoom) {
        return chatRoomRepository.save(
                ChatRoomJpaEntity.from(chatRoom)
        ).getId();
    }

    @Override
    public void updateLastMessageAtByChatRoomId(LocalDateTime createdAt, Long chatRoomId) {
        chatRoomRepository.updateLastMessageAtByChatRoomId(
                createdAt, chatRoomId);
    }
}
