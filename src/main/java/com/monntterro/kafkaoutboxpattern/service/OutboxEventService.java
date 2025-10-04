package com.monntterro.kafkaoutboxpattern.service;

import com.monntterro.kafkaoutboxpattern.entity.EventStatus;
import com.monntterro.kafkaoutboxpattern.entity.OutboxEvent;
import com.monntterro.kafkaoutboxpattern.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Limit;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OutboxEventService {
    private final OutboxEventRepository outboxEventRepository;

    public void save(OutboxEvent event) {
        outboxEventRepository.save(event);
    }

    public List<OutboxEvent> findByStatusOrderByCreatedAt(EventStatus eventStatus, int limit) {
        return outboxEventRepository.findByStatusOrderByCreatedAt(eventStatus, Limit.of(limit));
    }
}
