package io.playground.chatservice.infrastructure.persistence.chat.repository;

import io.playground.chatservice.infrastructure.persistence.chat.entity.ChatMessageJpaEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageJpaRepository extends JpaRepository<ChatMessageJpaEntity, Long> {
    @EntityGraph(attributePaths = {"parentMessage"})
    List<ChatMessageJpaEntity> findAllByChatRoomIdOrderByCreatedAtDesc(Long chatRoomId);

    boolean existsByIdAndChatRoomId(Long chatMessageId, Long chatRoomId);
}

// { chatRoomId & createdAt Desc }, { id & chatRoomId }