package com.monntterro.kafkaoutboxpattern.controller;

import com.monntterro.kafkaoutboxpattern.entity.Order;
import com.monntterro.kafkaoutboxpattern.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

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
    public Order create(@RequestParam("canFail") boolean canFail, @RequestBody Order order) {
        return orderService.create(order, canFail);
    }

    @PostMapping("/many")
    public Order createManyRandom(@RequestParam("canFail") boolean canFail, @RequestParam("count") Long count,
                                  @RequestBody Order order) {
        while (count-- > 1) {
            orderService.create(order, canFail);
        }
        return orderService.create(order, canFail);
    }
}
