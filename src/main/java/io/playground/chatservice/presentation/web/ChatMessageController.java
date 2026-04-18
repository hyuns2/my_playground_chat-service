package io.playground.chatservice.presentation.web;

import io.playground.chatservice.application.chat.command.GetChatMessagesCommand;
import io.playground.chatservice.application.chat.command.SendChatMessageCommand;
import io.playground.chatservice.application.chat.service.ChatMessageUsecase;
import io.playground.securitycore.core.AuthPrincipal;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ChatMessageController {
    private final ChatMessageUsecase chatMessageUsecase;

    @GetMapping("/messages/{chatRoomId}")
    @PreAuthorize("hasAuthority('USER')")
    public ResponseEntity<ChatResponseDto.GetChatMessagesInfo> getChatMessages(@AuthenticationPrincipal AuthPrincipal authPrincipal,
                                                                               @PathVariable Long chatRoomId) {
        return ResponseEntity.ok(
                ChatResponseDto.GetChatMessagesInfo.from(
                        chatMessageUsecase.getChatMessages(
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
        chatMessageUsecase.sendChatMessage(
                SendChatMessageCommand.from(dto)
        );
    }
}
