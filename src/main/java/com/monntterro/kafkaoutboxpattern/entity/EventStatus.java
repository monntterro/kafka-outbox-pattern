package com.monntterro.kafkaoutboxpattern.entity;

public enum EventStatus {
    CREATED,
    SENT,
    FAILED,
    PERMANENTLY_FAILED
}
