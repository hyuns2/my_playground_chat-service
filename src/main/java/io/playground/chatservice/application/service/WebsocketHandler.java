package io.playground.chatservice.application.service;

import io.playground.chatservice.application.command.SendChatMessageCommand;
import io.playground.chatservice.application.dto.ChatDto;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessageSendingOperations;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class WebsocketHandler implements WebsocketUsecase {
    private final SimpMessageSendingOperations messageSendingOperations;

    @Override
    public void sendChatMessage(SendChatMessageCommand command) {
        messageSendingOperations.convertAndSend(
                "/sub/chat-room" + command.chatRoomId(),
                ChatDto.ChatMessageInfoForPubSub.of(
                        command.senderId(),
                        command.type(),
                        command.content(),
                        command.parentMessageId(),
                        command.createdAt()
                )
        );
    }
}
