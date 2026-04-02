package io.playground.chatservice.application.port;

public interface EventPublisherPort {
    void publish(Object event);

    void createStreamForChatRoom(Long chatRoomId);
}
