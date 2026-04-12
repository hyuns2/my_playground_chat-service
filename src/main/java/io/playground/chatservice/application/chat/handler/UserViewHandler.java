package io.playground.chatservice.application.chat.handler;

import io.playground.chatservice.application.read.model.UserProfileUpdatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class UserViewHandler implements MessageHandler<UserProfileUpdatedEvent> {
    private final EventHandler eventHandler;

    @Override
    public void handle(UserProfileUpdatedEvent message) {
        eventHandler.handleUserProfileUpdatedEvent(message);
    }
}
