package net.ehicks.rabbitmqdemo;

import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ListenerContainer
{
    SimpleMessageListenerContainer container;

    public ListenerContainer(SimpleMessageListenerContainer container)
    {
        this.container = container;
    }

    public void setConsumers(int consumers)
    {
        container.setConcurrentConsumers(consumers);
    }

    public int getConsumers()
    {
        return container.getActiveConsumerCount();
    }

    public SimpleMessageListenerContainer getContainer()
    {
        return container;
    }
}
