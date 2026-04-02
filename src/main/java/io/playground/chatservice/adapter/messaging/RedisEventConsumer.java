package io.playground.chatservice.adapter.messaging;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.playground.chatservice.common.config.RedisStreamConfig;
import io.playground.chatservice.common.config.RedisStreamName;
import io.playground.chatservice.common.exception.CustomErrorCode;
import io.playground.chatservice.common.exception.CustomException;
import io.playground.chatservice.domain.event.EventType;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.stream.StreamListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisEventConsumer implements StreamListener<String, MapRecord<String, String, String>> {
    private final StringRedisTemplate stringRedisTemplate;
    private final ObjectMapper objectMapper;
    private final RedisEventHandler eventHandler;
    private final RedisChatEventHandler chatEventHandler;
    private final static String CHAT_STREAM_NAME = RedisStreamName.CHAT_STREAM.getValue();
    private final static String CHAT_ROOM_STREAM_NAME = RedisStreamName.CHAT_ROOM_STREAM.getValue();
    private final static String CHAT_GROUP_NAME = RedisStreamConfig.CHAT_GROUP_NAME;

    @Override
    public void onMessage(MapRecord<String, String, String> message) {
        handleMessage(message);
    }

    private void handleMessage(MapRecord<String, String, String> message) {
        try {
            EventEnvelope envelope = objectMapper.convertValue(message.getValue(), EventEnvelope.class);

            if (envelope.getEventType().equals(EventType.USER_PROFILE_CREATED_EVENT.getValue())) {
                eventHandler.handleUserProfileCreatedEvent(envelope);
                ack(CHAT_STREAM_NAME, CHAT_GROUP_NAME, message.getId().getValue());
            }

            else if (envelope.getEventType().equals(EventType.CHAT_MESSAGE_SENT_EVENT.getValue())) {
                chatEventHandler.handleChatMessageSentEventForDB(envelope);
                chatEventHandler.handleChatMessageSentEventForPubSub(envelope);
                ack(CHAT_ROOM_STREAM_NAME, CHAT_GROUP_NAME + "1", message.getId().getValue());
                ack(CHAT_ROOM_STREAM_NAME, CHAT_GROUP_NAME + "2", message.getId().getValue());
            }

            else
                throw new CustomException(CustomErrorCode.UNKNOWN_EVENT_TYPE);
        } catch (CustomException e) {
            throw e;
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void ack(String streamName, String groupName, String messageId) {
        stringRedisTemplate.opsForStream()
                .acknowledge(
                        streamName,
                        groupName,
                        messageId
                );
    }
}
