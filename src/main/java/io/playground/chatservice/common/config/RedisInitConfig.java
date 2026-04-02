package io.playground.chatservice.common.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.connection.stream.ReadOffset;
import org.springframework.data.redis.core.StringRedisTemplate;

@Configuration
@RequiredArgsConstructor
public class RedisInitConfig {
    private final StringRedisTemplate stringRedisTemplate;
    private final static String CHAT_STREAM_NAME = RedisStreamName.CHAT_STREAM.getValue();
    private final static String CHAT_GROUP_NAME = RedisStreamConfig.CHAT_GROUP_NAME;

    public void streamInitializer() {
        try {
            stringRedisTemplate.opsForStream()
                    .createGroup(
                            CHAT_STREAM_NAME,
                            ReadOffset.latest(),
                            CHAT_GROUP_NAME
                    );
        } catch (Exception ignored) {
        }
    }
}
