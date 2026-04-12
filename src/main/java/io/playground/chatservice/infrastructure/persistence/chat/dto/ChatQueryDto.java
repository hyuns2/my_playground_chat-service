package io.playground.chatservice.infrastructure.persistence.chat.dto;

import io.playground.chatservice.domain.chat.message.ChatMessage.MessageType;
import io.playground.chatservice.domain.chat.room.ChatRoom.RoomType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class ChatQueryDto {
    @Getter
    @AllArgsConstructor
    public static class ChatRoomInfo {
        Long chatRoomId;

        RoomType type;

        String name;

        List<ParticipantInfo> participantInfos;

        public static ChatRoomInfo of(Long chatRoomId, RoomType type, String name, List<ParticipantInfo> participantInfos) {
            return new ChatRoomInfo(chatRoomId,
                    type,
                    name,
                    participantInfos);
        }
    }

    @Getter
    @AllArgsConstructor
    public static class ParticipantInfo {
        String id;

        String nickName;

        boolean isAdmin;

        public static ParticipantInfo of(String participantId, String nickName, boolean isAdmin) {
            return new ParticipantInfo(
                    participantId,
                    nickName,
                    isAdmin
            );
        }
    }

    @Getter
    @AllArgsConstructor
    public static class ChatMessagesInfo {
        Map<String, Long> lastReadMessageIdInfos;

        List<ChatMessage> chatMessages;

        public static ChatMessagesInfo of(Map<String, Long> lastReadMessageIdInfos,
                                          List<ChatMessage> chatMessages) {
            return new ChatMessagesInfo(
                    lastReadMessageIdInfos,
                    chatMessages
            );
        }
    }

    @Getter
    @AllArgsConstructor
    public static class ChatMessage {
        Long chatMessageId;

        Long chatRoomId;

        String senderId;

        MessageType type;

        String content;

        Long parentMessageId;

        LocalDateTime createdAt;

        public static ChatMessage of(Long chatMessageId,
                                     Long chatRoomId,
                                     String senderId,
                                     MessageType type,
                                     String content,
                                     Long parentMessageId,
                                     LocalDateTime createdAt) {
            return new ChatMessage(
                    chatMessageId,
                    chatRoomId,
                    senderId,
                    type,
                    content,
                    parentMessageId,
                    createdAt
            );
        }
    }
}
