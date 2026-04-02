package io.playground.chatservice.application.service;

import io.playground.chatservice.application.command.CreateChatMessageCommand;
import io.playground.chatservice.application.command.CreateChatRoomCommand;
import io.playground.chatservice.application.command.GetChatMessagesCommand;
import io.playground.chatservice.application.command.SendChatMessageCommand;
import io.playground.chatservice.application.dto.ChatDto;
import io.playground.chatservice.domain.chat.ChatRoom;

import java.util.List;

public interface ChatUsecase {
    ChatRoom createChatRoom(CreateChatRoomCommand command);

    List<ChatDto.GetChatRoomsResult> getChatRooms(String userId);

    ChatDto.GetChatMessagesResult getChatMessages(GetChatMessagesCommand command);

    void sendChatMessage(SendChatMessageCommand command);

    void createChatMessage(CreateChatMessageCommand command);
}
