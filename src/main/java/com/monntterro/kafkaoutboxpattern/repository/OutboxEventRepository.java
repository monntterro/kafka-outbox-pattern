package com.monntterro.kafkaoutboxpattern.repository;

import com.monntterro.kafkaoutboxpattern.entity.EventStatus;
import com.monntterro.kafkaoutboxpattern.entity.OutboxEvent;
import org.springframework.data.domain.Limit;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface OutboxEventRepository extends JpaRepository<OutboxEvent, Long> {

    List<OutboxEvent> findByStatusOrderByCreatedAt(EventStatus status, Limit limit);
}