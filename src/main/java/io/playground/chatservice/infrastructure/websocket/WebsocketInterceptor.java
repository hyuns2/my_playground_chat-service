package io.playground.chatservice.infrastructure.websocket;

import io.playground.securitycore.core.AuthPrincipal;
import io.playground.securitycore.core.JwtAuthenticationProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.messaging.Message;
import org.springframework.messaging.MessageChannel;
import org.springframework.messaging.simp.stomp.StompCommand;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.messaging.support.ChannelInterceptor;
import org.springframework.messaging.support.MessageHeaderAccessor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class WebsocketInterceptor implements ChannelInterceptor {
    private final JwtAuthenticationProvider jwtAuthenticationProvider;
    private final WebsocketSessionHandler sessionHandler;

    @Override
    public Message<?> preSend(Message<?> message, MessageChannel channel) {
        try {
            StompHeaderAccessor accessor = MessageHeaderAccessor.getAccessor(message, StompHeaderAccessor.class);

            assert accessor != null;
            if (StompCommand.CONNECT.equals(accessor.getCommand())) {
                // 일단 grant type 제외해서 토큰만 전달하도록!
                Authentication authentication = jwtAuthenticationProvider.getAuthentication(
                        accessor.getFirstNativeHeader(HttpHeaders.AUTHORIZATION)
                );

                AuthPrincipal authPrincipal = (AuthPrincipal) authentication.getPrincipal();
                sessionHandler.onConnect(authPrincipal.userId(), authPrincipal.deviceId());
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        // ToDo: 채팅 송수신 인증/인가 로직 구현 필요

        return message;
    }
}
