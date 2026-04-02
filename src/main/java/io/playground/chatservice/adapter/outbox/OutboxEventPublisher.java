package io.playground.chatservice.adapter.outbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.playground.chatservice.application.port.EventPublisherPort;
import io.playground.chatservice.common.config.RedisStreamConfig;
import io.playground.chatservice.common.config.RedisStreamName;
import io.playground.chatservice.common.exception.CustomErrorCode;
import io.playground.chatservice.common.exception.CustomException;
import io.playground.chatservice.domain.event.ChatMessageSentEvent;
import io.playground.chatservice.domain.event.EventType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class OutboxEventPublisher implements EventPublisherPort {
    private final EventLogJpaRepository eventLogJpaRepository;
    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;
    private final static String CHAT_ROOM_STREAM_NAME = RedisStreamName.CHAT_ROOM_STREAM.getValue();
    public final static String CHAT_GROUP_FOR_DB_NAME = RedisStreamConfig.CHAT_GROUP_FOR_DB_NAME;
    public final static String CHAT_GROUP_FOR_PUBSUB_NAME = RedisStreamConfig.CHAT_GROUP_FOR_PUBSUB_NAME;

    @Override
    public void publish(Object event) {
        if (ChatMessageSentEvent.class.equals(event.getClass()))
            handle(EventType.CHAT_MESSAGE_SENT_EVENT, event);

        else
            throw new CustomException(CustomErrorCode.UNKNOWN_EVENT_TYPE);
    }

    private void handle(EventType eventType, Object event) {
        try {
            eventLogJpaRepository.save(
                    EventLogEntity.builder()
                            .eventType(eventType)
                            .payload(objectMapper.writeValueAsString(event))
                            .build()
            );
        } catch (Exception e) {
            throw new CustomException(CustomErrorCode.PUBLISHING_FAILED);
        }
    }

    @Override
    public void createStreamForChatRoom(Long chatRoomId) {
        try {
            stringRedisTemplate.opsForStream()
                    .createGroup(
                            CHAT_ROOM_STREAM_NAME + chatRoomId,
                            ReadOffset.from("0-0"),
                            CHAT_GROUP_FOR_DB_NAME
                    );

            stringRedisTemplate.opsForStream()
                    .createGroup(
                            CHAT_ROOM_STREAM_NAME + chatRoomId,
                            ReadOffset.from("0-0"),
                            CHAT_GROUP_FOR_PUBSUB_NAME
                    );
        } catch (Exception ignored) {
        }
    }
}
