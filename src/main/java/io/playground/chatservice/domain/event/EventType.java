package io.playground.chatservice.domain.event;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum EventType {
    // sub
    USER_PROFILE_CREATED_EVENT(0, "user-profile-created-event"),

    // all
    CHAT_MESSAGE_SENT_EVENT(1, "chat_message_sent_event");

    private final int code;
    private final String value;
}
