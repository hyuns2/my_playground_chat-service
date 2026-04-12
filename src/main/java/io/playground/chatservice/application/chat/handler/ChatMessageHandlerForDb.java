package io.playground.chatservice.application.chat.handler;

import io.playground.chatservice.domain.chat.message.ChatMessageSentEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatMessageHandlerForDb implements MessageHandler<ChatMessageSentEvent> {
    private final EventHandler eventHandler;

    @Override
    public void handle(ChatMessageSentEvent event) {
        eventHandler.handleChatMessageSentEventForDb(event);
    }
}
