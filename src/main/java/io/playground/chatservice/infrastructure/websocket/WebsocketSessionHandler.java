package io.playground.chatservice.infrastructure.websocket;

import io.playground.chatservice.application.chat.handler.ChatMessageHandlerForDb;
import io.playground.chatservice.application.chat.manager.WebSocketSessionManager;
import io.playground.chatservice.application.chat.service.ChatUsecase;
import io.playground.chatservice.application.eventstream.EventGroupType;
import io.playground.chatservice.application.eventstream.EventStreamNamingStrategy;
import io.playground.chatservice.application.eventstream.EventStreamType;
import io.playground.chatservice.domain.chat.message.ChatMessageSentEvent;
import io.playground.chatservice.infrastructure.messaging.consumer.RedisEventStreamListenerManager;
import io.playground.chatservice.infrastructure.pubsub.ChatMessageHandlerForPub;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Set;

@Slf4j
@Component
@RequiredArgsConstructor
public class WebsocketSessionHandler {
    private final ChatUsecase chatUsecase;
    private final WebSocketSessionManager sessionManager;
    private final RedisEventStreamListenerManager listenerManager;
    private final EventStreamNamingStrategy namingStrategy;
    private final ChatMessageHandlerForDb chatMessageHandlerForDb;
    private final ChatMessageHandlerForPub chatMessageHandlerForPub;
    private final static String ONLY_CONSUMER = "only-consumer";

    public void onConnect(String userId, String deviceId) {
        List<Long> chatRoomIds = chatUsecase.getChatRoomIds(userId);

        Set<Long> newChatRoomIds = sessionManager.addSession(
                userId,
                deviceId,
                chatRoomIds
        );

        for (Long chatRoomId : newChatRoomIds) {
            String streamName =  namingStrategy.toStreamName(
                    EventStreamType.CHAT_MESSAGES,
                    chatRoomId.toString()
            );

            listenerManager.subscribe(
                    streamName,
                    namingStrategy.toGroupName(
                            EventStreamType.CHAT_MESSAGES,
                            chatRoomId.toString(),
                            EventGroupType.DB_WRITER
                    ),
                    ONLY_CONSUMER,
                    ChatMessageSentEvent.class,
                    chatMessageHandlerForDb
            );
            listenerManager.subscribe(
                    streamName,
                    namingStrategy.toGroupName(
                            EventStreamType.CHAT_MESSAGES,
                            chatRoomId.toString(),
                            EventGroupType.MESSAGE_PUBLISHER
                    ),
                    ONLY_CONSUMER,
                    ChatMessageSentEvent.class,
                    chatMessageHandlerForPub
            );
        }
    }

    public void onDisconnect(String userId, String deviceId) {
        Set<Long> emptyRoomIds = sessionManager.removeSession(userId, deviceId);

        if (emptyRoomIds == null || emptyRoomIds.isEmpty())
            return;

        for (Long chatRoomId : emptyRoomIds) {
            String streamName =  namingStrategy.toStreamName(
                    EventStreamType.CHAT_MESSAGES,
                    chatRoomId.toString()
            );

            listenerManager.unsubscribe(
                    streamName,
                    namingStrategy.toGroupName(
                            EventStreamType.CHAT_MESSAGES,
                            chatRoomId.toString(),
                            EventGroupType.DB_WRITER
                    ),
                    ONLY_CONSUMER
            );
            listenerManager.unsubscribe(
                    streamName,
                    namingStrategy.toGroupName(
                            EventStreamType.CHAT_MESSAGES,
                            chatRoomId.toString(),
                            EventGroupType.MESSAGE_PUBLISHER
                    ),
                    ONLY_CONSUMER
            );
        }
    }
}
