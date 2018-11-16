package net.ehicks.rabbitmqdemo;

import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.stereotype.Component;

import java.util.Random;

@Component
public class Worker
{
    private final RabbitTemplate rabbitTemplate;

    public Worker(RabbitTemplate rabbitTemplate)
    {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void receiveMessage(String message)
    {
        message = message.substring(message.indexOf("."));
        String threadName = Thread.currentThread().getName();
        String key = threadName.substring(threadName.indexOf("-") + 1);
        ConsumerMap.consumerMap.put(key, new ConsumerRecord(key, message, 0));

        Random r = new Random();

        int wait = 0;
        for (char aChar : message.toCharArray())
        {
            if (aChar == '.')
            {
                wait+=100;
            }
        }

        int divider = wait / 100;
        for (int i = 0; i < wait; i++)
        {
            int progress = i / divider;
            ConsumerMap.consumerMap.get(key).setProgress(progress);
            sleep(10);

            if (r.nextInt(100) < 2)
                sleep(r.nextInt(1000));
        }

        ConsumerMap.consumerMap.remove(key);
//        ConsumerMap.consumerMap.get(key).setProgress(0);
    }

    public void sleep(int n)
    {
        try
        {
            Thread.sleep(n);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}
