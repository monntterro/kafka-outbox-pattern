package com.monntterro.kafkaoutboxpattern.utils;

public class Utils {

    public static void simulateFailure(float chancePercent) {
        if (Math.random() < chancePercent) {
            throw new RuntimeException("Simulated transient failure");
        }
    }
}
