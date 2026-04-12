package io.playground.chatservice.infrastructure.persistence.chat.adapter;

import io.playground.chatservice.application.chat.port.ChatParticipantRepositoryPort;
import io.playground.chatservice.domain.chat.ChatParticipant;
import io.playground.chatservice.infrastructure.persistence.chat.dto.ChatQueryDto;
import io.playground.chatservice.infrastructure.persistence.chat.entity.ChatParticipantJpaEntity;
import io.playground.chatservice.infrastructure.persistence.chat.entity.ChatRoomJpaEntity;
import io.playground.chatservice.infrastructure.persistence.chat.repository.ChatParticipantJpaRepository;
import io.playground.chatservice.infrastructure.persistence.chat.repository.ChatRoomJpaRepository;
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
    public List<ChatQueryDto.ChatRoomInfo> findChatInfosByParticipantId(String participantId) {
        List<ChatRoomJpaEntity> roomEntities = chatParticipantRepository
                .findAllByParticipantId(participantId).stream()
                .map(ChatParticipantJpaEntity::getChatRoom)
                .toList();

        List<ChatParticipantJpaEntity> participantEntities = chatParticipantRepository
                .findAllByChatRoomIdIn(
                        roomEntities.stream()
                                .map(ChatRoomJpaEntity::getId)
                                .toList()
                );

        Map<ChatRoomJpaEntity, List<ChatParticipantJpaEntity>> groupedByChatRoom = participantEntities.stream()
                .collect(
                        Collectors.groupingBy(ChatParticipantJpaEntity::getChatRoom)
                );

        List<ChatQueryDto.ChatRoomInfo> result = new ArrayList<>();
        for (ChatRoomJpaEntity roomEntity : roomEntities) {
            result.add(
                    ChatQueryDto.ChatRoomInfo.of(
                            roomEntity.getId(),
                            roomEntity.getType(),
                            roomEntity.getName(),
                            groupedByChatRoom.get(roomEntity).stream()
                                    .map(participant -> ChatQueryDto.ParticipantInfo.of(
                                            participant.getParticipantId(),
                                            participant.getNickName(),
                                            participant.isAdmin()
                                    )).toList()
                    )
            );
        }

        return result;
    }

    @Override
    public Map<String, Long> findLastReadMessageIdInfosByChatRoomId(Long chatRoomId) {
        Map<String, Long> lastReadMessageIdInfos = new HashMap<>();

        for (ChatParticipantJpaEntity entity : chatParticipantRepository.findAllByChatRoomId(chatRoomId))
            lastReadMessageIdInfos.put(
                            entity.getParticipantId(),
                            entity.getLastReadMessageId()
            );

        return lastReadMessageIdInfos;
    }

    @Override
    public List<Long> findChatRoomIdsByParticipantId(String participantId) {
        return chatParticipantRepository.findAllByParticipantId(participantId).stream()
                .map(chatParticipantEntity -> chatParticipantEntity.getChatRoom().getId())
                .toList();
    }

    @Override
    public void saveAll(List<ChatParticipant> chatParticipants) {
        chatParticipantRepository.saveAll(
                chatParticipants.stream()
                        .map(chatParticipant ->
                                ChatParticipantJpaEntity.from(
                                        chatParticipant,
                                        toReferenceEntity(chatParticipant.getChatRoomId())
                                )
                        ).toList()
        );
    }

    @Override
    public void updateLastReadMessageIdByParticipantId(Long lastReadMessageId, String participantId) {
        chatParticipantRepository.updateLastReadMessageIdByParticipantId(
                lastReadMessageId, participantId);
    }

    private ChatRoomJpaEntity toReferenceEntity(Long chatRoomId) {
        return chatRoomRepository.getReferenceById(chatRoomId);
    }
}
