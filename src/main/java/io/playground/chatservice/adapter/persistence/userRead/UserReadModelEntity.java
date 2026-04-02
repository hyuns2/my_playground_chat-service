package io.playground.chatservice.adapter.persistence.userRead;

import io.playground.chatservice.domain.userRead.UserReadModel;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Builder
public class UserReadModelEntity {
    @Id
    private String userId;

    private String nickName;

    private boolean pushAgree;

    private LocalDateTime updatedAt;

    public void update(UserReadModel userReadModel) {
        this.userId = userReadModel.getUserId();
        this.nickName = userReadModel.getNickName();
        this.pushAgree = userReadModel.isPushAgree();
        this.updatedAt = userReadModel.getUpdatedAt();
    }
}
