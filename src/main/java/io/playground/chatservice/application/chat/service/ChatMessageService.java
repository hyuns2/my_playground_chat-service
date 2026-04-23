package io.playground.chatservice.application.chat.service;

import io.playground.chatservice.application.chat.command.GetChatMessagesCommand;
import io.playground.chatservice.application.chat.command.SendChatMessageCommand;
import io.playground.chatservice.application.chat.dto.ChatDto;
import io.playground.chatservice.application.chat.port.ChatMessageRepositoryPort;
import io.playground.chatservice.application.chat.port.ChatParticipantRepositoryPort;
import io.playground.chatservice.application.chat.port.ChatRoomRepositoryPort;
import io.playground.chatservice.application.chat.port.EventPublisherPort;
import io.playground.chatservice.application.eventstream.PubEventType;
import io.playground.chatservice.domain.chat.message.ChatMessage;
import io.playground.chatservice.domain.chat.message.ChatMessageSentEvent;
import io.playground.chatservice.exception.CustomErrorCode;
import io.playground.chatservice.exception.CustomException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class ChatMessageService implements ChatMessageUsecase {
    private final ChatRoomRepositoryPort chatRoomRepositoryPort;
    private final ChatParticipantRepositoryPort chatParticipantRepositoryPort;
    private final ChatMessageRepositoryPort chatMessageRepositoryPort;
    private final EventPublisherPort eventPublisherPort;

    @Override
    @Transactional
    public Long sendChatMessage(SendChatMessageCommand command) {
        if (!chatParticipantRepositoryPort.existsByChatRoomIdAndParticipantId(
                command.chatRoomId(), command.senderId()
        ))
            throw new CustomException(CustomErrorCode.INVALID_CHAT_PARTICIPANT);

        if (command.parentMessageId() != null
                && !chatMessageRepositoryPort.existsByIdAndChatRoomId(
                command.parentMessageId(), command.chatRoomId()))
            throw new CustomException(CustomErrorCode.INVALID_PARENT_MESSAGE);

        Long chatMessageId = saveChatMessageAndUpdateInfo(command);

        eventPublisherPort.publish(
                PubEventType.CHAT_MESSAGE_SENT,
                command.chatRoomId().toString(),
                ChatMessageSentEvent.of(
                        chatMessageId,
                        command.chatRoomId(),
                        command.senderId(),
                        command.type(),
                        command.content(),
                        command.parentMessageId(),
                        command.createdAt()
                )
        );

        return chatMessageId;
    }

    private Long saveChatMessageAndUpdateInfo(SendChatMessageCommand command) {
        chatRoomRepositoryPort.updateLastMessageAtByChatRoomId(
                command.createdAt(),
                command.chatRoomId()
        );

        return chatMessageRepositoryPort.save(
                ChatMessage.of(
                        null,
                        command.chatRoomId(),
                        command.senderId(),
                        command.type(),
                        command.content(),
                        command.parentMessageId(),
                        command.createdAt()
                )
        );
    }

//    @Override
//    @Transactional
//    public ChatDto.ChatMessagesInfo getChatMessages(GetChatMessagesCommand command) {
//        if (!chatParticipantRepositoryPort.existsByChatRoomIdAndParticipantId(
//                command.chatRoomId(), command.userId()
//        ))
//            throw new CustomException(CustomErrorCode.INVALID_CHAT_PARTICIPANT);
//
//        Map<String, Long> lastReadMessageIdInfos = chatParticipantRepositoryPort
//                .findLastReadMessageIdInfosByChatRoomId(command.chatRoomId());
//        List<ChatDto.ChatMessageInfo> chatMessageInfos = chatMessageRepositoryPort
//                .findAllByChatRoomIdOrderByCreatedAtDesc(
//                        command.chatRoomId(),
//                        PageRequest.of(command.page(),
//                                command.size(),
//                                Sort.by(Sort.Direction.DESC, "createdAt")
//                        )
//                );
//
//        if (command.page() == 0 && !chatMessageInfos.isEmpty())
//            chatParticipantRepositoryPort.updateLastReadMessageIdByParticipantId(
//                    chatMessageInfos.get(0).getChatMessageId(),
//                    command.userId()
//            );
//
//        return ChatDto.ChatMessagesInfo.of(
//                lastReadMessageIdInfos, chatMessageInfos);
//    }

    @Override
    @Transactional
    public ChatDto.ChatMessagesInfo getChatMessages(GetChatMessagesCommand command) {
        Map<String, Long> lastReadMessageIdInfos = chatParticipantRepositoryPort
                .findLastReadMessageIdInfosByChatRoomId(command.chatRoomId());
        if (lastReadMessageIdInfos.isEmpty())
            throw new CustomException(CustomErrorCode.INVALID_CHAT_PARTICIPANT);

        List<ChatDto.ChatMessageInfo> chatMessageInfos = chatMessageRepositoryPort
                .findAllByChatRoomIdOrderByCreatedAtDesc(
                        command.chatRoomId(),
                        PageRequest.of(command.page(),
                                command.size(),
                                Sort.by(Sort.Direction.DESC, "createdAt")
                        )
                );

        if (command.page() == 0 && !chatMessageInfos.isEmpty())
            chatParticipantRepositoryPort.updateLastReadMessageIdByParticipantId(
                    chatMessageInfos.get(0).getChatMessageId(),
                    command.userId()
            );

        return ChatDto.ChatMessagesInfo.of(
                lastReadMessageIdInfos, chatMessageInfos);
    }
}
