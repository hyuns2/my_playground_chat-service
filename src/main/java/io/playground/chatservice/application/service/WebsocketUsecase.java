package io.playground.chatservice.application.service;

import io.playground.chatservice.application.command.SendChatMessageCommand;

public interface WebsocketUsecase {
    void sendChatMessage(SendChatMessageCommand command);
}
