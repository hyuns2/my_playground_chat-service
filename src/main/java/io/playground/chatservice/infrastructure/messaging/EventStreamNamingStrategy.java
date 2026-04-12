package io.playground.chatservice.infrastructure.messaging;

import io.playground.chatservice.application.eventstream.EventGroupType;
import io.playground.chatservice.application.eventstream.EventStreamType;
import org.springframework.stereotype.Component;

@Component
public class EventStreamNamingStrategy implements io.playground.chatservice.application.eventstream.EventStreamNamingStrategy {
    @Override
    public String toStreamName(EventStreamType streamType, String key) {
        return switch (streamType) {
            case CHAT_EVENTS ->
                    streamType.getPrefix();
            case CHAT_MESSAGES ->
                    streamType.getPrefix() + key;
        };
    }

    @Override
    public String toGroupName(EventStreamType streamType, String key, EventGroupType groupType) {
        return toStreamName(streamType, key) + groupType.getValue();
    }
}
