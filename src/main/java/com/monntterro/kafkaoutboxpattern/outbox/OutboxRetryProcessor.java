package com.monntterro.kafkaoutboxpattern.outbox;

import com.monntterro.kafkaoutboxpattern.entity.EventStatus;
import com.monntterro.kafkaoutboxpattern.service.OutboxEventService;
import com.monntterro.kafkaoutboxpattern.utils.Utils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class OutboxRetryProcessor {
    private final OutboxEventService outboxEventService;
    private final KafkaTemplate<String, String> kafkaTemplate;

    @Value("${outbox.kafka.topic}")
    private String outboxTopic;

    @Value("${outbox.scheduler.retries.limit}")
    private Integer limit;

    @Value("${outbox.max-retries}")
    private Integer maxRetries;

    @Scheduled(fixedDelayString = "${outbox.scheduler.retries.delay}")
    public void retryProcessOutboxEvents() {
        outboxEventService.findByStatusOrderByCreatedAt(EventStatus.FAILED, limit).forEach(outbox -> {
            outbox.increaseRetries();

            EventStatus status = EventStatus.SENT;
            try {
                Utils.simulateFailure(0.5f);
                kafkaTemplate.send(outboxTopic, outbox.getPayload()).get();
            } catch (Exception e) {
                log.warn("Failed to resend outbox event with id {}", outbox.getId());
                status = EventStatus.FAILED;
            }

            if (status == EventStatus.FAILED && outbox.getRetries() >= maxRetries) {
                log.error("Outbox event with id {} has reached max retries. Marking as PERMANENTLY_FAILED.", outbox.getId());
                status = EventStatus.PERMANENTLY_FAILED;
            }

            outbox.setStatus(status);
            outboxEventService.save(outbox);
            log.info("Retried outbox event with id {}", outbox.getId());
        });
    }
}
