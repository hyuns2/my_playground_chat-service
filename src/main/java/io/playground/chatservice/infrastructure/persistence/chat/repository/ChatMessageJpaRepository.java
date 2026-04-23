package io.playground.chatservice.infrastructure.persistence.chat.repository;

import io.playground.chatservice.infrastructure.persistence.chat.entity.ChatMessageJpaEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageJpaRepository extends JpaRepository<ChatMessageJpaEntity, Long> {
    @EntityGraph(attributePaths = {"parentMessage", "parentMessage.id"})
    List<ChatMessageJpaEntity> findPageByChatRoomIdOrderByCreatedAtDesc(Long chatRoomId, Pageable pageable);

    boolean existsByIdAndChatRoomId(Long chatMessageId, Long chatRoomId);
}
