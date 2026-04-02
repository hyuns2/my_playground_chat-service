package io.playground.chatservice.adapter.persistence.chat.repository;

import io.playground.chatservice.adapter.persistence.chat.entity.ChatParticipantEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ChatParticipantJpaRepository extends JpaRepository<ChatParticipantEntity, Long> {
    boolean existsByChatRoomIdAndParticipantId(Long chatRoomId, String participantId);

    List<ChatParticipantEntity> findAllByParticipantId(String participantId);

    List<ChatParticipantEntity> findAllByChatRoomIdIn(List<Long> chatRoomIds);

    List<ChatParticipantEntity> findAllByChatRoomId(Long chatRoomId);

    @Modifying
    @Query("UPDATE ChatParticipantEntity cp " +
            "SET cp.lastReadMessageId = :lastReadMessageId " +
            "WHERE cp.participantId = :participantId")
    void updateLastReadMessageIdByParticipantId(Long lastReadMessageId, String participantId);
}


// {chatRoomId, ParticipantId}
