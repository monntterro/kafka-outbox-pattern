package com.monntterro.kafkaoutboxpattern.controller;

import com.fasterxml.jackson.databind.JsonNode;
import com.monntterro.kafkaoutboxpattern.entity.Order;
import com.monntterro.kafkaoutboxpattern.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping
    public List<Order> getAll() {
        return orderService.getAll();
    }

    @GetMapping("/{id}")
    public Order getOne(@PathVariable Long id) {
        return orderService.getOne(id);
    }

    @PostMapping
    public Order create(@RequestBody Order order) {
        return orderService.create(order);
    }

    @DeleteMapping("/{id}")
    public Order delete(@PathVariable Long id) {
        return orderService.delete(id);
    }
}
