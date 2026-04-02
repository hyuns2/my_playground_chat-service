package io.playground.chatservice.adapter.outbox;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.playground.chatservice.adapter.messaging.RedisStreamPublisher;
import io.playground.chatservice.common.config.RedisStreamName;
import io.playground.chatservice.common.exception.CustomErrorCode;
import io.playground.chatservice.common.exception.CustomException;
import io.playground.chatservice.domain.event.ChatMessageSentEvent;
import io.playground.chatservice.domain.event.EventType;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OutboxPoller {
    private final EventLogJpaRepository eventLogJpaRepository;
    private final RedisStreamPublisher redisStreamPublisher;
    private final ObjectMapper objectMapper;
    private final static String CHAT_ROOM_STREAM_NAME = RedisStreamName.CHAT_ROOM_STREAM.getValue();

    @Scheduled(fixedDelay = 5000)
    @Transactional
    public void publishEvents() {
        List<EventLogEntity> eventLogs = eventLogJpaRepository.findAllByProcessedFalse();

        for (EventLogEntity eventLog : eventLogs) {
            try {
                if (eventLog.getEventType().equals(EventType.CHAT_MESSAGE_SENT_EVENT)) {
                    ChatMessageSentEvent event = objectMapper.readValue(eventLog.getPayload(), ChatMessageSentEvent.class);
                    redisStreamPublisher.publish(
                            CHAT_ROOM_STREAM_NAME + event.chatRoomId(),
                            eventLog);
                }

                else
                    throw new CustomException(CustomErrorCode.UNKNOWN_EVENT_TYPE);

                eventLog.setProcessed(true);
            } catch (Exception e) {
                eventLog.setProcessed(false);
            }
        }
    }
}
