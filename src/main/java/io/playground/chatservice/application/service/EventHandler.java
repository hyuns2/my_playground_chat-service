package io.playground.chatservice.application.service;

import io.playground.chatservice.application.command.CreateChatMessageCommand;
import io.playground.chatservice.application.command.SendChatMessageCommand;
import io.playground.chatservice.application.port.UserReadModelRepositoryPort;
import io.playground.chatservice.domain.event.ChatMessageSentEvent;
import io.playground.chatservice.domain.event.UserProfileCreatedEvent;
import io.playground.chatservice.domain.userRead.UserReadModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class EventHandler implements EventUsecase {
    private final UserReadModelRepositoryPort userReadModelRepositoryPort;
    private final ChatUsecase chatUsecase;
    private final WebsocketUsecase websocketUsecase;

    @Override
    public void handleUserProfileCreatedEvent(UserProfileCreatedEvent event) {
        UserReadModel model = userReadModelRepositoryPort.findById(event.userId())
                .orElse(
                        UserReadModel.of(
                                event.userId(),
                                event.nickName(),
                                event.pushAgree(),
                                event.createdAt()
                        )
                );

        model.update(event.nickName(), event.pushAgree(), event.createdAt());

        userReadModelRepositoryPort.saveOrUpdate(model);
    }

    @Override
    public void handleChatMessageSentEventForDB(ChatMessageSentEvent event) {
        chatUsecase.createChatMessage(
                CreateChatMessageCommand.builder()
                        .chatRoomId(event.chatRoomId())
                        .senderId(event.senderId())
                        .type(event.type())
                        .content(event.content())
                        .parentMessageId(event.parentMessageId())
                        .createdAt(event.createdAt())
                        .build()
        );
    }

    @Override
    public void handleChatMessageSentEventForPubSub(ChatMessageSentEvent event) {
        websocketUsecase.sendChatMessage(
                SendChatMessageCommand.builder()
                        .chatRoomId(event.chatRoomId())
                        .senderId(event.senderId())
                        .type(event.type())
                        .content(event.content())
                        .parentMessageId(event.parentMessageId())
                        .createdAt(event.createdAt())
                        .build()
        );
    }
}
