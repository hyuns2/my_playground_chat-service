package io.playground.chatservice.common.config;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum RedisStreamName {
    // event
    AUTH_STREAM(0, "auth.events"),
    USER_STREAM(1, "user.events"),
    CHAT_STREAM(2, "chat.events"),

    // chat-room
    CHAT_ROOM_STREAM(3, "chat.chat-room.");

    private final int code;
    private final String value;
}
