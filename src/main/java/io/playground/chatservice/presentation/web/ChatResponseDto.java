package io.playground.chatservice.presentation.web;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.playground.chatservice.domain.chat.message.ChatMessage;
import io.playground.chatservice.domain.chat.room.ChatRoom;
import io.playground.chatservice.infrastructure.persistence.chat.dto.ChatQueryDto;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public class ChatResponseDto {
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetChatRoomInfo {
        Long id;

        ChatRoom.RoomType type;

        String name;

        List<ParticipantInfo> participantInfos;

        public static GetChatRoomInfo from(ChatQueryDto.ChatRoomInfo queryDto) {
            return new GetChatRoomInfo(
                    queryDto.getChatRoomId(),
                    queryDto.getType(),
                    queryDto.getName(),
                    queryDto.getParticipantInfos().stream()
                            .map(ParticipantInfo::from)
                            .toList()
            );
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    private static class ParticipantInfo {
        String id;

        String nickName;

        @JsonProperty("admin")
        boolean isAdmin;

        private static ParticipantInfo from(ChatQueryDto.ParticipantInfo info) {
            return new ParticipantInfo(
                    info.getId(),
                    info.getNickName(),
                    info.isAdmin()
            );
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class GetChatMessagesInfo {
        Map<String, Long> lastReadMessageIdInfos;

        List<ChatMessageInfo> chatMessageInfos;

        public static GetChatMessagesInfo from(ChatQueryDto.ChatMessagesInfo result) {
            return new GetChatMessagesInfo(
                    result.getLastReadMessageIdInfos(),
                    result.getChatMessages().stream()
                            .map(ChatMessageInfo::from)
                            .toList()
            );
        }
    }

    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    private static class ChatMessageInfo {
        Long chatMessageId;

        String senderId;

        ChatMessage.MessageType type;

        String content;

        Long parentMessageId;

        LocalDateTime createdAt;

        public static ChatMessageInfo from(ChatQueryDto.ChatMessage info) {
            return new ChatMessageInfo(
                    info.getChatMessageId(),
                    info.getSenderId(),
                    info.getType(),
                    info.getContent(),
                    info.getParentMessageId(),
                    info.getCreatedAt()
            );
        }
    }
}
