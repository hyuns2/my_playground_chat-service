package io.playground.chatservice.infrastructure.persistence.chat.adapter;

import io.playground.chatservice.application.chat.port.ChatMessageRepositoryPort;
import io.playground.chatservice.domain.chat.message.ChatMessage;
import io.playground.chatservice.infrastructure.persistence.chat.dto.ChatQueryDto;
import io.playground.chatservice.infrastructure.persistence.chat.entity.ChatMessageJpaEntity;
import io.playground.chatservice.infrastructure.persistence.chat.entity.ChatRoomJpaEntity;
import io.playground.chatservice.infrastructure.persistence.chat.repository.ChatMessageJpaRepository;
import io.playground.chatservice.infrastructure.persistence.chat.repository.ChatRoomJpaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class ChatMessagePersistenceAdapter implements ChatMessageRepositoryPort {
    private final ChatMessageJpaRepository chatMessageRepository;
    private final ChatRoomJpaRepository chatRoomRepository;

    @Override
    public List<ChatQueryDto.ChatMessage> findAllByChatRoomIdOrderByCreatedAtDesc(Long chatRoomId) {
        return chatMessageRepository.findAllByChatRoomIdOrderByCreatedAtDesc(chatRoomId).stream()
                .map(ChatMessageJpaEntity::toDto)
                .toList();
    }

    @Override
    public boolean existsByIdAndChatRoomId(Long chatMessageId, Long chatRoomId) {
        return chatMessageRepository.existsByIdAndChatRoomId(chatMessageId, chatRoomId);
    }

    @Override
    public void save(ChatMessage chatMessage) {
        chatMessageRepository.save(
                    ChatMessageJpaEntity.from(
                            chatMessage,
                            toReferenceChatRoomEntity(chatMessage.getChatRoomId()),
                            chatMessage.getParentMessageId() != null
                                    ? toReferenceChatMessageEntity(chatMessage.getParentMessageId())
                                    : null
                    )
        );
    }

    private ChatRoomJpaEntity toReferenceChatRoomEntity(Long chatRoomId) {
        return chatRoomRepository.getReferenceById(chatRoomId);
    }

    private ChatMessageJpaEntity toReferenceChatMessageEntity(Long chatMessageId) {
        return chatMessageRepository.getReferenceById(chatMessageId);
    }
}
