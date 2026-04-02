package io.playground.chatservice.adapter.outbox;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EventLogJpaRepository extends JpaRepository<EventLogEntity, String> {
    List<EventLogEntity> findAllByProcessedFalse();
}
