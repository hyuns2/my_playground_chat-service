package io.playground.chatservice.adapter.persistence.chat.entity;

import io.playground.chatservice.domain.chat.ChatMessage;
import io.playground.chatservice.domain.chat.MessageType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = lombok.AccessLevel.PROTECTED)
@AllArgsConstructor
public class ChatMessageEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoomEntity chatRoom;

    @Column(unique = true, nullable = false)
    private String senderId;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private MessageType type;

    @Column(nullable = false)
    private String content;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_message_id")
    private ChatMessageEntity parentMessage;

    @Column(nullable = false, updatable = false)
    @CreationTimestamp
    private LocalDateTime createdAt;

    public static ChatMessageEntity of(Long id,
                                       ChatRoomEntity chatRoom,
                                       String senderId,
                                       MessageType type,
                                       String content,
                                       ChatMessageEntity parentMessage,
                                       LocalDateTime createdAt) {
        return new ChatMessageEntity(
                id,
                chatRoom,
                senderId,
                type,
                content,
                parentMessage,
                createdAt
        );
    }

    public ChatMessage toDomain() {
        return ChatMessage.of(
                this.id,
                this.chatRoom.getId(),
                this.senderId,
                this.type,
                this.content,
                this.parentMessage != null ? this.parentMessage.getId() : null,
                this.createdAt
        );
    }
}
