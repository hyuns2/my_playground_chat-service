package io.playground.chatservice.application.chat.port;

import io.playground.chatservice.application.chat.dto.ChatDto;
import io.playground.chatservice.domain.chat.message.ChatMessage;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ChatMessageRepositoryPort {
    boolean existsByIdAndChatRoomId(Long chatMessageId, Long chatRoomId);

    Long save(ChatMessage chatMessage);

    List<ChatDto.ChatMessageInfo> findAllByChatRoomIdOrderByCreatedAtDesc(Long chatRoomId, Pageable pageable);
}
