package io.playground.chatservice.adapter.persistence.chat.repository;

import io.playground.chatservice.adapter.persistence.chat.entity.ChatParticipantEntity;
import io.playground.chatservice.adapter.persistence.chat.entity.ChatRoomEntity;
import io.playground.chatservice.application.dto.ChatDto;
import io.playground.chatservice.application.port.ChatParticipantRepositoryPort;
import io.playground.chatservice.domain.chat.ChatParticipant;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ChatParticipantPersistenceAdapter implements ChatParticipantRepositoryPort {
    private final ChatParticipantJpaRepository chatParticipantRepository;
    private final ChatRoomJpaRepository chatRoomRepository;

    @Override
    public boolean existsByChatRoomIdAndParticipantId(Long chatRoomId, String participantId) {
        return chatParticipantRepository
                .existsByChatRoomIdAndParticipantId(chatRoomId, participantId);
    }

    @Override
    public List<ChatDto.GetChatRoomsResult> findChatInfosByParticipantId(String participantId) {
        List<ChatRoomEntity> chatRooms = chatParticipantRepository
                .findAllByParticipantId(participantId).stream()
                .map(ChatParticipantEntity::getChatRoom)
                .toList();

        List<ChatParticipantEntity> participantsFlatInfo = chatParticipantRepository
                .findAllByChatRoomIdIn(
                        chatRooms.stream().map(ChatRoomEntity::getId).toList()
                );

        Map<ChatRoomEntity, List<ChatParticipantEntity>> groupedByChatRoom = participantsFlatInfo.stream()
                .collect(
                        Collectors.groupingBy(ChatParticipantEntity::getChatRoom)
                );

        List<ChatDto.GetChatRoomsResult> results = new ArrayList<>();
        for (ChatRoomEntity chatRoom : chatRooms) {
            results.add(
                    ChatDto.GetChatRoomsResult.of(
                            chatRoom.getId(),
                            chatRoom.getType(),
                            chatRoom.getName(),
                            groupedByChatRoom.get(chatRoom).stream()
                                    .map(participant -> ChatDto.ParticipantInfo.of(
                                            participant.getParticipantId(),
                                            participant.getNickName(),
                                            participant.isAdmin()
                                    )).toList()
                    )
            );
        }

        return results;
    }

    @Override
    public Map<String, Long> findLastReadMessageIdInfosByChatRoomId(Long chatRoomId) {
        Map<String, Long> lastReadMessageIdInfos = new HashMap<>();

        for (ChatParticipantEntity entity : chatParticipantRepository.findAllByChatRoomId(chatRoomId))
            lastReadMessageIdInfos.put(
                            entity.getParticipantId(),
                            entity.getLastReadMessageId()
            );

        return lastReadMessageIdInfos;
    }

    @Override
    public void saveAll(List<ChatParticipant> chatParticipants) {
        chatParticipantRepository.saveAll(
                chatParticipants.stream()
                        .map(chatParticipant ->
                                ChatParticipantEntity.of(
                                        toReferenceEntity(chatParticipant.getChatRoomId()),
                                        chatParticipant.getParticipantId(),
                                        chatParticipant.getNickName(),
                                        chatParticipant.isAdmin(),
                                        chatParticipant.getLastReadMessageId()
                                )
                        ).toList()
        );
    }

    private ChatRoomEntity toReferenceEntity(Long chatRoomId) {
        return chatRoomRepository.getReferenceById(chatRoomId);
    }

    @Override
    public void updateLastReadMessageIdByParticipantId(Long lastReadMessageId, String participantId) {
        chatParticipantRepository.updateLastReadMessageIdByParticipantId(
                lastReadMessageId, participantId);
    }
}
