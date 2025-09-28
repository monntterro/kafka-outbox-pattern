package com.monntterro.kafkaoutboxpattern.service;

import com.monntterro.kafkaoutboxpattern.entity.EventStatus;
import com.monntterro.kafkaoutboxpattern.entity.OutboxEvent;
import com.monntterro.kafkaoutboxpattern.repository.OutboxEventRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OutboxEventService {
    private final OutboxEventRepository outboxEventRepository;

    public OutboxEvent save(OutboxEvent event) {
        return outboxEventRepository.save(event);
    }

    public List<OutboxEvent> findAllByStatus(EventStatus eventStatus) {
        return outboxEventRepository.findAllByStatus(eventStatus);
    }
}
