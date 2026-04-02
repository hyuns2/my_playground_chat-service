package io.playground.chatservice.adapter.persistence.userRead;

import org.springframework.data.jpa.repository.JpaRepository;

public interface UserReadModelJpaRepository extends JpaRepository<UserReadModelEntity, String> {
}
