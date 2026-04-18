package io.playground.chatservice.application.chat.service;

import io.playground.chatservice.application.chat.command.GetChatMessagesCommand;
import io.playground.chatservice.application.chat.command.SendChatMessageCommand;
import io.playground.chatservice.infrastructure.persistence.chat.dto.ChatQueryDto;

public interface ChatMessageUsecase {
    ChatQueryDto.ChatMessagesInfo getChatMessages(GetChatMessagesCommand command);

    void sendChatMessage(SendChatMessageCommand command);
}
