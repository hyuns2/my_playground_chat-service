package io.playground.chatservice.common.config;

import io.playground.chatservice.adapter.messaging.RedisEventConsumer;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.stream.Consumer;
import org.springframework.data.redis.connection.stream.MapRecord;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.connection.stream.StreamOffset;
import org.springframework.data.redis.stream.StreamMessageListenerContainer;

import java.time.Duration;

@Configuration
@RequiredArgsConstructor
public class RedisStreamConfig {
    private final RedisInitConfig redisInitConfig;
    private final static String CHAT_STREAM_NAME = RedisStreamName.CHAT_STREAM.getValue();
    public final static String CHAT_GROUP_NAME = "chat-group";
    public final static String CHAT_GROUP_FOR_DB_NAME = "chat-group-db";
    public final static String CHAT_GROUP_FOR_PUBSUB_NAME = "chat-group-pubsub";

    @Bean
    public StreamMessageListenerContainer<String, MapRecord<String, String, String>> streamListenerContainer(
            RedisConnectionFactory connectionFactory,
            RedisEventConsumer redisEventConsumer) {
        redisInitConfig.streamInitializer();

        StreamMessageListenerContainer<String, MapRecord<String, String, String>> container =
                StreamMessageListenerContainer.create(
                        connectionFactory,
                        StreamMessageListenerContainer.StreamMessageListenerContainerOptions.builder()
                                .pollTimeout(Duration.ofSeconds(2))
                                .build());

        container.receive(
                Consumer.from(CHAT_GROUP_NAME, "consumer-1"),
                    StreamOffset.create(CHAT_STREAM_NAME, ReadOffset.lastConsumed()),
                redisEventConsumer
        );
        container.receive(
                Consumer.from(CHAT_GROUP_NAME, "consumer-2"),
                StreamOffset.create(CHAT_STREAM_NAME, ReadOffset.lastConsumed()),
                redisEventConsumer
        );

        container.start();
        return container;
    }
}
