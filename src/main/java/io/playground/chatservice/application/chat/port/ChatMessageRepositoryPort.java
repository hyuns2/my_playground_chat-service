package io.playground.chatservice.application.chat.port;

import io.playground.chatservice.domain.chat.message.ChatMessage;
import io.playground.chatservice.infrastructure.persistence.chat.dto.ChatQueryDto;

import java.util.List;

public interface ChatMessageRepositoryPort {

    List<ChatQueryDto.ChatMessage> findAllByChatRoomIdOrderByCreatedAtDesc(Long chatRoomId);

    boolean existsByIdAndChatRoomId(Long chatMessageId, Long chatRoomId);

    void save(ChatMessage chatMessage);
}
