package io.playground.chatservice.adapter.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.playground.chatservice.application.service.EventUsecase;
import io.playground.chatservice.domain.event.UserProfileCreatedEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisEventHandler {
    private final ObjectMapper objectMapper;
    private final EventUsecase eventUsecase;

    public void handleUserProfileCreatedEvent(EventEnvelope envelope) throws JsonProcessingException {
        eventUsecase.handleUserProfileCreatedEvent(
                objectMapper.readValue(envelope.getPayload(), UserProfileCreatedEvent.class)
        );
    }
}
