package io.playground.chatservice.adapter.persistence.chat.repository;

import io.playground.chatservice.adapter.persistence.chat.entity.ChatRoomEntity;
import io.playground.chatservice.application.port.ChatRoomRepositoryPort;
import io.playground.chatservice.domain.chat.ChatRoom;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@RequiredArgsConstructor
public class ChatRoomPersistenceAdapter implements ChatRoomRepositoryPort {
    private final ChatRoomJpaRepository repository;

    @Override
    public ChatRoom save(ChatRoom chatRoom) {
        return repository.save(ChatRoomEntity.from(chatRoom))
                .toDomain();
    }

    @Override
    public void updateLastMessageAtByChatRoomId(LocalDateTime createdAt, Long chatRoomId) {
        repository.updateLastMessageAtByChatRoomId(createdAt, chatRoomId);
    }
}
