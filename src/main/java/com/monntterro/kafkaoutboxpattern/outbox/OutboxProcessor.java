package com.monntterro.kafkaoutboxpattern.outbox;

import com.monntterro.kafkaoutboxpattern.entity.EventStatus;
import com.monntterro.kafkaoutboxpattern.entity.OutboxEvent;
import com.monntterro.kafkaoutboxpattern.service.OutboxEventService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class OutboxProcessor {
    private final OutboxEventService outboxEventService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${outbox.kafka.topic}")
    private String outboxTopic;

    @Scheduled(fixedDelay = 1000)
    public void processOutboxEvents() {
        outboxEventService.findAllByStatus(EventStatus.CREATED).forEach(outbox -> {
            EventStatus status = EventStatus.SENT;
            try {
                kafkaTemplate.send(outboxTopic, outbox.getPayload());
            } catch (Exception e) {
                status = EventStatus.FAILED;
            }

            outbox.setStatus(status);
            outboxEventService.save(outbox);
        });
    }
}