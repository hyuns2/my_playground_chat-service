package io.playground.chatservice.application.dto;

import io.playground.chatservice.domain.chat.MessageType;
import io.playground.chatservice.domain.chat.RoomType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class ChatDto {
    @Getter
    @AllArgsConstructor
    public static class GetChatRoomsResult {
        Long chatRoomId;

        RoomType type;

        String name;

        List<ParticipantInfo> participantInfos;

        public static GetChatRoomsResult of(Long chatRoomId, RoomType type, String name, List<ParticipantInfo> participantInfos) {
            return new GetChatRoomsResult(chatRoomId,
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
    public static class GetChatMessagesResult {
        Map<String, Long> lastReadMessageIdInfos;

        List<ChatMessageInfoForDB> chatMessageInfoForDBS;

        public static GetChatMessagesResult of(Map<String, Long> lastReadMessageIdInfos,
                                            List<ChatMessageInfoForDB> chatMessageInfoForDBS) {
            return new GetChatMessagesResult(
                    lastReadMessageIdInfos,
                    chatMessageInfoForDBS
            );
        }
    }

    @Getter
    @AllArgsConstructor
    public static class ChatMessageInfoForDB {
        Long chatMessageId;

        Long chatRoomId;

        String senderId;

        MessageType messageType;

        String content;

        Long parentMessageId;

        LocalDateTime createdAt;

        public static ChatMessageInfoForDB of(Long chatMessageId,
                                              Long chatRoomId,
                                              String senderId,
                                              MessageType messageType,
                                              String content,
                                              Long parentMessageId,
                                              LocalDateTime createdAt) {
            return new ChatMessageInfoForDB(
                    chatMessageId,
                    chatRoomId,
                    senderId,
                    messageType,
                    content,
                    parentMessageId,
                    createdAt
            );
        }
    }

    @Getter
    @AllArgsConstructor
    public static class ChatMessageInfoForPubSub {
        String senderId;

        MessageType messageType;

        String content;

        Long parentMessageId;

        LocalDateTime createdAt;

        public static ChatMessageInfoForPubSub of(String senderId,
                                              MessageType messageType,
                                              String content,
                                              Long parentMessageId,
                                              LocalDateTime createdAt) {
            return new ChatMessageInfoForPubSub(
                    senderId,
                    messageType,
                    content,
                    parentMessageId,
                    createdAt
            );
        }
    }
}
