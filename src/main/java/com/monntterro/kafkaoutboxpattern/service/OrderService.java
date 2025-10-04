package com.monntterro.kafkaoutboxpattern.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.monntterro.kafkaoutboxpattern.entity.EventStatus;
import com.monntterro.kafkaoutboxpattern.entity.Order;
import com.monntterro.kafkaoutboxpattern.entity.OutboxEvent;
import com.monntterro.kafkaoutboxpattern.repository.OrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class OrderService {
    private static final ObjectMapper objectMapper = new ObjectMapper();

    private final OrderRepository orderRepository;
    private final OutboxEventService outboxEventService;

    public List<Order> getAll() {
        return orderRepository.findAll();
    }

    public Order getOne(Long id) {
        Optional<Order> orderOptional = orderRepository.findById(id);
        return orderOptional.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Entity with id `%s` not found".formatted(id)));
    }

    @Transactional
    public Order create(Order o, boolean canFail) {
        Order order = orderRepository.save(o);

        OutboxEvent outboxEvent = new OutboxEvent();
        try {
            outboxEvent.setPayload(objectMapper.writeValueAsString(order));
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }

        // Simulate random failure
        if (canFail && Math.random() < 0.4) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Random failure occurred");
        }

        outboxEvent.setStatus(EventStatus.CREATED);
        outboxEventService.save(outboxEvent);

        return order;
    }
}
