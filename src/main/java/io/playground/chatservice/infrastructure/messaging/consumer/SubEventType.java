package io.playground.chatservice.infrastructure.messaging.consumer;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum SubEventType {
    USER_PROFILE_UPDATED(0, "user.profile.updated"),
    CHAT_MESSAGE_SENT(1, "chat.message.sent");

    private final int code;
    private final String value;
}
