package io.playground.chatservice.application.port;

import io.playground.chatservice.application.dto.ChatDto;
import io.playground.chatservice.domain.chat.ChatMessage;

import java.util.List;

public interface ChatMessageRepositoryPort {
    List<ChatDto.ChatMessageInfoForDB> findAllByChatRoomIdOrderByCreatedAtDesc(Long chatRoomId);

    boolean existsByIdAndChatRoomId(Long chatMessageId, Long chatRoomId);

    ChatMessage save(ChatMessage chatMessage);
}
