package io.playground.chatservice.adapter.persistence.chat.entity;

import io.playground.chatservice.domain.chat.ChatRoom;
import io.playground.chatservice.domain.chat.RoomType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ChatRoomEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private RoomType type;

    @Column
    private String name;

    @Column
    private LocalDateTime lastMessageAt;
    
    public static ChatRoomEntity from(ChatRoom chatRoom) {
        return ChatRoomEntity.builder()
                .type(chatRoom.getType())
                .name(chatRoom.getName())
                .lastMessageAt(chatRoom.getLastMessagedAt())
                .build();
    }

    public ChatRoom toDomain() {
        return ChatRoom.of(id, type, name, lastMessageAt);
    }
}
