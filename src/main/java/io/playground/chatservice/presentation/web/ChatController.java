package io.playground.chatservice.presentation.web;

import io.playground.chatservice.application.chat.command.CreateChatRoomCommand;
import io.playground.chatservice.application.chat.command.GetChatMessagesCommand;
import io.playground.chatservice.application.chat.command.SendChatMessageCommand;
import io.playground.chatservice.application.chat.service.ChatUsecase;
import io.playground.securitycore.core.AuthPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class ChatController {
    private final ChatUsecase chatUsecase;

    @PostMapping("/room")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<Long> createChatRoom(@AuthenticationPrincipal AuthPrincipal authPrincipal,
                                               @Valid @RequestBody ChatRequestDto.CreateChatRoom dto) {
        return ResponseEntity.ok(
                chatUsecase.createChatRoom(
                        CreateChatRoomCommand.from(authPrincipal.userId(), dto)
                )
        );
    }

    @GetMapping("/rooms")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<List<ChatResponseDto.GetChatRoomInfo>> getChatRooms(@AuthenticationPrincipal AuthPrincipal authPrincipal) {
        return ResponseEntity.ok(
                chatUsecase.getChatRooms(authPrincipal.userId()).stream()
                        .map(ChatResponseDto.GetChatRoomInfo::from)
                        .toList()
        );
    }

    @GetMapping("/messages/{chatRoomId}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<ChatResponseDto.GetChatMessagesInfo> getChatMessages(@AuthenticationPrincipal AuthPrincipal authPrincipal,
                                                                               @PathVariable Long chatRoomId) {
        return ResponseEntity.ok(
                ChatResponseDto.GetChatMessagesInfo.from(
                        chatUsecase.getChatMessages(
                                GetChatMessagesCommand.of(
                                        authPrincipal.userId(),
                                        chatRoomId
                                )
                        )
                )
        );
    }

    @MessageMapping("/chat-room")
    public void sendChatMessage(@Valid @RequestBody ChatRequestDto.SendChatMessage dto) {
        chatUsecase.sendChatMessage(
                SendChatMessageCommand.from(dto)
        );
    }
}
