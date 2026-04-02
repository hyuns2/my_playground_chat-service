package io.playground.chatservice.adapter.web;

import io.playground.chatservice.application.command.CreateChatRoomCommand;
import io.playground.chatservice.application.command.GetChatMessagesCommand;
import io.playground.chatservice.application.command.SendChatMessageCommand;
import io.playground.chatservice.application.service.ChatUsecase;
import io.playground.securitycore.core.AuthPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
public class ChatController {
    private final ChatUsecase chatUsecase;

    @PostMapping("/chat-room")
    @PreAuthorize("hasRole('USER')")
    public ResponseEntity<Long> createChatRoom(@AuthenticationPrincipal AuthPrincipal authPrincipal, @Valid @RequestBody ChatRequestDto.CreateChatRoom dto) {
        CreateChatRoomCommand command = CreateChatRoomCommand.builder()
                .creatorId(authPrincipal.userId())
                .type(dto.getType())
                .name(dto.getName())
                .participantIds(dto.getParticipantIds())
                .build();

        return ResponseEntity.ok(
                chatUsecase.createChatRoom(command)
                        .getId()
        );
    }

    @GetMapping("/chat-room")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<List<ChatResponseDto.GetChatRoomInfo>> getChatRooms(@AuthenticationPrincipal AuthPrincipal authPrincipal) {
        List<ChatResponseDto.GetChatRoomInfo> responses = chatUsecase.getChatRooms(authPrincipal.userId()).stream()
                .map(ChatResponseDto.GetChatRoomInfo::from)
                .toList();

        return ResponseEntity.ok(responses);
    }

    @GetMapping("/chat-message/{chatRoomId}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<ChatResponseDto.GetChatMessagesInfo> getChatMessage(@AuthenticationPrincipal AuthPrincipal authPrincipal, @PathVariable Long chatRoomId) {
        GetChatMessagesCommand command = GetChatMessagesCommand.builder()
                .userId(authPrincipal.userId())
                .chatRoomId(chatRoomId)
                .build();

        return ResponseEntity.ok(
                ChatResponseDto.GetChatMessagesInfo.from(
                        chatUsecase.getChatMessages(command)
                )
        );
    }

    @MessageMapping("/chat-message")
    public void sendChatMessage(@Valid @RequestBody ChatRequestDto.SendChatMessage dto) {
        SendChatMessageCommand command = SendChatMessageCommand.builder()
                .senderId(dto.getSenderId())
                .chatRoomId(dto.getChatRoomId())
                .type(dto.getType())
                .content(dto.getContent())
                .parentMessageId(dto.getParentMessageId())
                .build();

        chatUsecase.sendChatMessage(command);
    }
}
