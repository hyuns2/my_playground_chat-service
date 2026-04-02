package io.playground.chatservice.application.service;

import io.playground.chatservice.domain.event.ChatMessageSentEvent;
import io.playground.chatservice.domain.event.UserProfileCreatedEvent;

public interface EventUsecase {
    void handleUserProfileCreatedEvent(UserProfileCreatedEvent event);

    void handleChatMessageSentEventForDB(ChatMessageSentEvent event);

    void handleChatMessageSentEventForPubSub(ChatMessageSentEvent event);
}
