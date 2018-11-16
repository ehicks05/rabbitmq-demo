package net.ehicks.rabbitmqdemo;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.stream.IntStream;

@Component
public class Runner implements CommandLineRunner
{
    private int messagesPerSecond = 1;
    private final RabbitTemplate rabbitTemplate;
    private AmqpAdmin amqpAdmin;
    private SimpleMessageListenerContainer simpleMessageListenerContainer;

    public Runner(RabbitTemplate rabbitTemplate, AmqpAdmin amqpAdmin,
                  SimpleMessageListenerContainer simpleMessageListenerContainer)
    {
        this.rabbitTemplate = rabbitTemplate;
        this.amqpAdmin = amqpAdmin;
        this.simpleMessageListenerContainer = simpleMessageListenerContainer;
    }

    public int getMessagesPerSecond()
    {
        return messagesPerSecond;
    }

    public void setMessagesPerSecond(int messagesPerSecond)
    {
        this.messagesPerSecond = messagesPerSecond;
    }

    @Override
    public void run(String... args) throws Exception
    {
        Random r = new Random();

        int i = 0;
        while (true)
        {
            if (i % 10 == 0)
            {
                Integer count = (Integer) amqpAdmin
                        .getQueueProperties(RabbitmqDemoApplication.queueName).get("QUEUE_MESSAGE_COUNT");

                int consumers = simpleMessageListenerContainer.getActiveConsumerCount();
                if (count > 100 && consumers < 256)
                {
                    consumers++;
                    simpleMessageListenerContainer.setConcurrentConsumers(consumers);
                }
            }

            int difficulty = r.nextInt(5) + 1;
            publishMessage(difficulty, getMessagesPerSecond());
            i++;
        }
    }

    private void publishMessage(int difficulty, int messagesPerSecond) throws InterruptedException
    {
        StringBuilder sb = new StringBuilder();
        IntStream.range(0, difficulty).forEach(number -> sb.append("."));
        rabbitTemplate.convertAndSend(RabbitmqDemoApplication.topicExchangeName,
                "workQueue.doWork", "Do Work: " + sb.toString());

        Thread.sleep(1000 / messagesPerSecond);
    }
}