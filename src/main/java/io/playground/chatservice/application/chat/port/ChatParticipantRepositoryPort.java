package io.playground.chatservice.application.chat.port;

import io.playground.chatservice.domain.chat.ChatParticipant;
import io.playground.chatservice.infrastructure.persistence.chat.dto.ChatQueryDto;

import java.util.List;
import java.util.Map;

public interface ChatParticipantRepositoryPort {
    boolean existsByChatRoomIdAndParticipantId(Long chatRoomId, String participantId);

    List<ChatQueryDto.ChatRoomInfo> findChatInfosByParticipantId(String participantId);

    Map<String, Long> findLastReadMessageIdInfosByChatRoomId(Long chatRoomId);

    List<Long> findChatRoomIdsByParticipantId(String participantId);

    void saveAll(List<ChatParticipant> chatParticipants);

    void updateLastReadMessageIdByParticipantId(Long lastReadMessageId, String participantId);
}
