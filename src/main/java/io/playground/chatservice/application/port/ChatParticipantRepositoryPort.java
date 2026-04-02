package io.playground.chatservice.application.port;

import io.playground.chatservice.application.dto.ChatDto;
import io.playground.chatservice.domain.chat.ChatParticipant;

import java.util.List;
import java.util.Map;

public interface ChatParticipantRepositoryPort {
    boolean existsByChatRoomIdAndParticipantId(Long chatRoomId, String participantId);

    List<ChatDto.GetChatRoomsResult> findChatInfosByParticipantId(String participantId);

    Map<String, Long> findLastReadMessageIdInfosByChatRoomId(Long chatRoomId);

    void saveAll(List<ChatParticipant> chatParticipants);

    void updateLastReadMessageIdByParticipantId(Long lastReadMessageId, String participantId);
}
