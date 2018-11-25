package net.ehicks.rabbitmqdemo;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Qualifier;

public class WorkerResponseStats
{
    private AmqpAdmin amqpAdmin;
    private SimpleMessageListenerContainer simpleMessageListenerContainer;

    public WorkerResponseStats(AmqpAdmin amqpAdmin,
                                   @Qualifier("workerResponseContainer")SimpleMessageListenerContainer simpleMessageListenerContainer)
    {
        this.amqpAdmin = amqpAdmin;
        this.simpleMessageListenerContainer = simpleMessageListenerContainer;
    }

    public int getActiveConsumers()
    {
        return simpleMessageListenerContainer.getActiveConsumerCount();
    }

    public int getQueuedMessages()
    {
        return (Integer) amqpAdmin
                .getQueueProperties(RabbitmqDemoApplication.queueName).get("QUEUE_MESSAGE_COUNT");
    }
}
