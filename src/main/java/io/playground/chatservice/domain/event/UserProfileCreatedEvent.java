package io.playground.chatservice.domain.event;

import java.time.LocalDateTime;

public record UserProfileCreatedEvent(
        String userId,
        String nickName,
        boolean pushAgree,
        LocalDateTime createdAt
) {
}
