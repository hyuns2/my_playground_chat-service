package io.playground.chatservice.adapter.persistence.chat.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class ChatParticipantEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "chat_room_id", nullable = false)
    private ChatRoomEntity chatRoom;

    @Column(unique = true, nullable = false)
    private String participantId;

    @Column(nullable = false)
    private String nickName;

    @Column(nullable = false)
    private boolean isAdmin;

    @Column
    private Long lastReadMessageId;

    public static ChatParticipantEntity of(ChatRoomEntity chatRoomEntity,
                                           String participantId,
                                           String nickName,
                                           boolean isAdmin,
                                           Long lastReadMessageId) {
        return ChatParticipantEntity.builder()
                .chatRoom(chatRoomEntity)
                .participantId(participantId)
                .nickName(nickName)
                .isAdmin(isAdmin)
                .lastReadMessageId(lastReadMessageId)
                .build();
    }
}
