package io.playground.chatservice.adapter.persistence.userRead;

import io.playground.chatservice.application.port.UserReadModelRepositoryPort;
import io.playground.chatservice.domain.userRead.UserReadModel;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
@RequiredArgsConstructor
public class UserReadModelRepositoryPortImpl implements UserReadModelRepositoryPort {
    private final UserReadModelJpaRepository jpaRepository;

    @Override
    public Optional<UserReadModel> findById(String userId) {
        return jpaRepository.findById(userId)
                .map(entity ->
                        new UserReadModel(
                                entity.getUserId(),
                                entity.getNickName(),
                                entity.isPushAgree(),
                                entity.getUpdatedAt()
                        )
                );
    }

    @Override
    public List<UserReadModel> findAllByIds(List<String> userIds) {
        return jpaRepository.findAllById(userIds).stream()
                .map(entity ->
                        new UserReadModel(
                                entity.getUserId(),
                                entity.getNickName(),
                                entity.isPushAgree(),
                                entity.getUpdatedAt()
                        )
                ).toList();
    }

    @Override
    public void saveOrUpdate(UserReadModel userReadModel) {
        UserReadModelEntity entity = jpaRepository.findById(userReadModel.getUserId())
                        .orElse(new UserReadModelEntity());

        entity.update(userReadModel);

        jpaRepository.save(entity);
    }
}
