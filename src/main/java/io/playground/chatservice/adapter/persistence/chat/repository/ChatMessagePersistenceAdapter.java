package io.playground.chatservice.adapter.persistence.chat.repository;

import io.playground.chatservice.adapter.persistence.chat.entity.ChatMessageEntity;
import io.playground.chatservice.adapter.persistence.chat.entity.ChatRoomEntity;
import io.playground.chatservice.application.dto.ChatDto;
import io.playground.chatservice.application.port.ChatMessageRepositoryPort;
import io.playground.chatservice.domain.chat.ChatMessage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ChatMessagePersistenceAdapter implements ChatMessageRepositoryPort {
    private final ChatMessageJpaRepository chatMessageRepository;
    private final ChatRoomJpaRepository chatRoomRepository;

    @Override
    public List<ChatDto.ChatMessageInfoForDB> findAllByChatRoomIdOrderByCreatedAtDesc(Long chatRoomId) {
        List<ChatMessageEntity> entities = chatMessageRepository.findAllByChatRoomIdOrderByCreatedAtDesc(chatRoomId);

        return entities.stream()
                .map(entity -> ChatDto.ChatMessageInfoForDB.of(
                        entity.getId(),
                        entity.getChatRoom().getId(),
                        entity.getSenderId(),
                        entity.getType(),
                        entity.getContent(),
                        entity.getParentMessage().getId(),
                        entity.getCreatedAt()
                )).toList();
    }

    @Override
    public boolean existsByIdAndChatRoomId(Long chatMessageId, Long chatRoomId) {
        return chatMessageRepository.existsByIdAndChatRoomId(chatMessageId, chatRoomId);
    }

    @Override
    public ChatMessage save(ChatMessage chatMessage) {
        ChatMessageEntity entity = chatMessageRepository.save(
                ChatMessageEntity.of(
                        chatMessage.getId(),
                        toReferenceChatRoomEntity(chatMessage.getChatRoomId()),
                        chatMessage.getSenderId(),
                        chatMessage.getType(),
                        chatMessage.getContent(),
                        chatMessage.getParentMessageId() != null
                                ? toReferenceChatMessageEntity(chatMessage.getParentMessageId())
                                : null,
                        chatMessage.getCreatedAt()
                )
        );

        return entity.toDomain();
    }

    private ChatRoomEntity toReferenceChatRoomEntity(Long chatRoomId) {
        return chatRoomRepository.getReferenceById(chatRoomId);
    }

    private ChatMessageEntity toReferenceChatMessageEntity(Long chatMessageId) {
        return chatMessageRepository.getReferenceById(chatMessageId);
    }
}
