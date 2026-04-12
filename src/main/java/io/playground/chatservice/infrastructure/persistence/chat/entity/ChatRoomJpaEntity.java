package io.playground.chatservice.infrastructure.persistence.chat.entity;

import io.playground.chatservice.domain.chat.room.ChatRoom;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
@Table(name = "CHAT_ROOM")
public class ChatRoomJpaEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ChatRoom.RoomType type;

    @Column
    private String name;

    @Column
    private LocalDateTime lastMessageAt;
    
    public static ChatRoomJpaEntity of(Long id,
                                         ChatRoom.RoomType type,
                                         String name,
                                         LocalDateTime lastMessageAt) {
        return new ChatRoomJpaEntity(
                id,
                type,
                name,
                lastMessageAt
        );
    }

    public static ChatRoomJpaEntity from(ChatRoom chatRoom) {
        return new ChatRoomJpaEntity(
                chatRoom.getId(),
                chatRoom.getType(),
                chatRoom.getName(),
                chatRoom.getLastMessagedAt()
        );
    }
}
