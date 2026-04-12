package io.playground.chatservice.infrastructure.persistence.chat.repository;

import io.playground.chatservice.infrastructure.persistence.chat.entity.ChatParticipantJpaEntity;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatParticipantJpaRepository extends JpaRepository<ChatParticipantJpaEntity, Long> {
    boolean existsByChatRoomIdAndParticipantId(Long chatRoomId, String participantId);

    @EntityGraph(attributePaths = {"chatRoom"})
    List<ChatParticipantJpaEntity> findAllByParticipantId(String participantId);

    @EntityGraph(attributePaths = {"chatRoom"})
    List<ChatParticipantJpaEntity> findAllByChatRoomIdIn(List<Long> chatRoomIds);

    List<ChatParticipantJpaEntity> findAllByChatRoomId(Long chatRoomId);

    @Modifying
    @Query("UPDATE ChatParticipantJpaEntity cp " +
            "SET cp.lastReadMessageId = :lastReadMessageId " +
            "WHERE cp.participantId = :participantId")
    void updateLastReadMessageIdByParticipantId(Long lastReadMessageId, String participantId);
}


// {chatRoomId, ParticipantId}
