package io.playground.chatservice.adapter.persistence.chat.repository;

import io.playground.chatservice.adapter.persistence.chat.entity.ChatMessageEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageJpaRepository extends JpaRepository<ChatMessageEntity, Long> {
    List<ChatMessageEntity> findAllByChatRoomIdOrderByCreatedAtDesc(Long chatRoomId);

    boolean existsByIdAndChatRoomId(Long chatMessageId, Long chatRoomId);
}

// { chatRoomId & createdAt Desc }, { id & chatRoomId }