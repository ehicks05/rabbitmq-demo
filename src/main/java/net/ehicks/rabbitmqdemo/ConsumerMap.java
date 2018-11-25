package net.ehicks.rabbitmqdemo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConsumerMap
{
    public static Map<Integer, ConsumerRecord> consumerMap = new ConcurrentHashMap<>();
}
