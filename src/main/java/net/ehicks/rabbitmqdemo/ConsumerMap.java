package net.ehicks.rabbitmqdemo;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class ConsumerMap
{
    public static Map<String, ConsumerRecord> consumerMap = new ConcurrentHashMap<>();
}
