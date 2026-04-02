package io.playground.chatservice.application.port;

import io.playground.chatservice.domain.userRead.UserReadModel;

import java.util.List;
import java.util.Optional;

public interface UserReadModelRepositoryPort {
    Optional<UserReadModel> findById(String userId);

    List<UserReadModel> findAllByIds(List<String> userIds);

    void saveOrUpdate(UserReadModel userReadModel);
}
