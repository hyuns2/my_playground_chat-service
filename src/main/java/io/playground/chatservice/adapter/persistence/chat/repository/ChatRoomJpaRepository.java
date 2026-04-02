package io.playground.chatservice.adapter.persistence.chat.repository;

import io.playground.chatservice.adapter.persistence.chat.entity.ChatRoomEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;

public interface ChatRoomJpaRepository extends JpaRepository<ChatRoomEntity, Long> {
    @Modifying
    @Query("update ChatRoomEntity c set c.lastMessageAt = :createdAt where c.id = :chatRoomId")
    void updateLastMessageAtByChatRoomId(LocalDateTime createdAt, Long chatRoomId);
}
