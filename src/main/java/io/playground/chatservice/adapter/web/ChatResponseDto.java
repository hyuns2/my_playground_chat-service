package io.playground.chatservice.adapter.web;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.playground.chatservice.application.dto.ChatDto;
import io.playground.chatservice.domain.chat.MessageType;
import io.playground.chatservice.domain.chat.RoomType;
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

        RoomType type;

        String name;

        List<ParticipantInfo> participantInfos;

        public static GetChatRoomInfo from(ChatDto.GetChatRoomsResult result) {
            List<ParticipantInfo> participantInfos = result.getParticipantInfos().stream()
                    .map(ParticipantInfo::from)
                    .toList();

            return new GetChatRoomInfo(
                    result.getChatRoomId(),
                    result.getType(),
                    result.getName(),
                    participantInfos
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

        private static ParticipantInfo from(ChatDto.ParticipantInfo info) {
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

        public static GetChatMessagesInfo from(ChatDto.GetChatMessagesResult result) {
            return new GetChatMessagesInfo(
                    result.getLastReadMessageIdInfos(),
                    result.getChatMessageInfoForDBS().stream()
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

        MessageType messageType;

        String content;

        Long parentMessageId;

        LocalDateTime createdAt;

        public static ChatMessageInfo from(ChatDto.ChatMessageInfoForDB info) {
            return new ChatMessageInfo(
                    info.getChatMessageId(),
                    info.getSenderId(),
                    info.getMessageType(),
                    info.getContent(),
                    info.getParentMessageId(),
                    info.getCreatedAt()
            );
        }
    }
}
