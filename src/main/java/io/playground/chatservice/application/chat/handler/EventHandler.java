package io.playground.chatservice.application.chat.handler;

import io.playground.chatservice.application.chat.command.SaveChatMessageCommand;
import io.playground.chatservice.domain.chat.message.ChatMessageSentEvent;
import io.playground.chatservice.application.chat.service.ChatUsecase;
import io.playground.chatservice.application.read.command.HandleUserProfileUpdatedCommand;
import io.playground.chatservice.application.read.model.UserProfileUpdatedEvent;
import io.playground.chatservice.application.read.service.UserViewUsecase;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventHandler {
    private final UserViewUsecase userViewUsecase;
    private final ChatUsecase chatUsecase;

    public void handleUserProfileUpdatedEvent(UserProfileUpdatedEvent event) {
        userViewUsecase.handleUserProfileUpdated(
                HandleUserProfileUpdatedCommand.from(event)
        );
    }

    public void handleChatMessageSentEventForDb(ChatMessageSentEvent event) {
        chatUsecase.saveChatMessage(
                SaveChatMessageCommand.from(event)
        );
    }
}
