package io.playground.chatservice.domain.userRead;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
@AllArgsConstructor
public class UserReadModel {
    private String userId;

    private String nickName;

    private boolean pushAgree;

    private LocalDateTime updatedAt;

    public static UserReadModel of(String userId,
                                   String nickName,
                                   boolean pushAgree,
                                   LocalDateTime updatedAt) {
        return new UserReadModel(userId, nickName, pushAgree, updatedAt);
    }

    public void update(String nickName, boolean pushAgree, LocalDateTime updatedAt) {
        if (updatedAt == null || updatedAt.isAfter(this.updatedAt)) {
            this.nickName = nickName;
            this.pushAgree = pushAgree;
            this.updatedAt = updatedAt;
        }
    }
}
