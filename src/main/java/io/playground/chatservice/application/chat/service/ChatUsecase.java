package io.playground.chatservice.application.chat.service;

import io.playground.chatservice.application.chat.command.CreateChatRoomCommand;
import io.playground.chatservice.application.chat.command.GetChatMessagesCommand;
import io.playground.chatservice.application.chat.command.SaveChatMessageCommand;
import io.playground.chatservice.application.chat.command.SendChatMessageCommand;
import io.playground.chatservice.infrastructure.persistence.chat.dto.ChatQueryDto;

import java.util.List;

public interface ChatUsecase {
    Long createChatRoom(CreateChatRoomCommand command);

    List<ChatQueryDto.ChatRoomInfo> getChatRooms(String userId);

    List<Long> getChatRoomIds(String userId);

    ChatQueryDto.ChatMessagesInfo getChatMessages(GetChatMessagesCommand command);

    void sendChatMessage(SendChatMessageCommand command);

    void saveChatMessage(SaveChatMessageCommand command);
}
