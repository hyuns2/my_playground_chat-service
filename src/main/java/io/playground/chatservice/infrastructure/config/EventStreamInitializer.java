package io.playground.chatservice.infrastructure.config;

import io.playground.chatservice.application.chat.handler.UserViewHandler;
import io.playground.chatservice.application.eventstream.EventGroupType;
import io.playground.chatservice.application.eventstream.EventStreamNamingStrategy;
import io.playground.chatservice.application.eventstream.EventStreamType;
import io.playground.chatservice.application.read.model.UserProfileUpdatedEvent;
import io.playground.chatservice.infrastructure.messaging.consumer.RedisEventStreamListenerManager;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class EventStreamInitializer {
    private final EventStreamNamingStrategy namingStrategy;
    private final RedisEventStreamListenerManager manager;
    private final UserViewHandler userViewHandler;

    @EventListener(ApplicationReadyEvent.class)
    public void init() {
        String chatEventsStreamName = namingStrategy.toStreamName(EventStreamType.CHAT_EVENTS, null);
        String chatEventsGroupName = namingStrategy.toGroupName(EventStreamType.CHAT_EVENTS, null, EventGroupType.VIEW_UPDATER);

        manager.subscribe(
                chatEventsStreamName,
                chatEventsGroupName,
                "consumer-1",
                UserProfileUpdatedEvent.class,
                userViewHandler
        );

        manager.subscribe(
                chatEventsStreamName,
                chatEventsGroupName,
                "consumer-2",
                UserProfileUpdatedEvent.class,
                userViewHandler
        );
    }
}
