package com.monntterro.kafkaoutboxpattern.repository;

import com.monntterro.kafkaoutboxpattern.entity.Order;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderRepository extends JpaRepository<Order, Long> {
}