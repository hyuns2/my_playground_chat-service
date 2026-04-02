package io.playground.chatservice.adapter.messaging;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.playground.chatservice.application.service.EventUsecase;
import io.playground.chatservice.domain.event.ChatMessageSentEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class RedisChatEventHandler {
    private final ObjectMapper objectMapper;
    private final EventUsecase eventUsecase;

    public void handleChatMessageSentEventForDB(EventEnvelope envelope) throws JsonProcessingException {
        eventUsecase.handleChatMessageSentEventForDB(
                objectMapper.readValue(envelope.getPayload(), ChatMessageSentEvent.class)
        );
    }

    public void handleChatMessageSentEventForPubSub(EventEnvelope envelope) throws JsonProcessingException {
        eventUsecase.handleChatMessageSentEventForPubSub(
                objectMapper.readValue(envelope.getPayload(), ChatMessageSentEvent.class)
        );
    }
}
