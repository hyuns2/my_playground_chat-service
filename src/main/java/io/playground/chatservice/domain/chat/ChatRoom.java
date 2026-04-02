package io.playground.chatservice.domain.chat;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class ChatRoom {
    private Long id;

    private RoomType type;

    private String name;

    private LocalDateTime lastMessagedAt;

    public static ChatRoom of(Long id,
                              RoomType type,
                              String name,
                              LocalDateTime lastMessagedAt) {
        return new ChatRoom(id, type, name, lastMessagedAt);
    }
}
