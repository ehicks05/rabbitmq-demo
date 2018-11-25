package net.ehicks.rabbitmqdemo;

import com.fasterxml.jackson.databind.ObjectMapper;
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
        Integer key = Integer.valueOf(threadName.substring(threadName.indexOf("-") + 1));

        ConsumerRecord consumerRecord = new ConsumerRecord(key, message, 0);
        ObjectMapper mapper = new ObjectMapper();
        sendProgressUpdate(consumerRecord, mapper);

        Random r = new Random();

        int wait = 0;
        for (char aChar : message.toCharArray())
            if (aChar == '.')
                wait += 100;

        long lastMessage = System.currentTimeMillis();

        int divider = wait / 100;
        for (int i = 0; i < wait; i++)
        {
            int progress = i / divider;
            
            if (System.currentTimeMillis() - lastMessage > 33)
            {
                consumerRecord.setProgress(progress);
                sendProgressUpdate(consumerRecord, mapper);

                lastMessage = System.currentTimeMillis();
            }

            sleep(10);

            if (r.nextInt(100) < 2)
                sleep(r.nextInt(1000));
        }

        consumerRecord.setProgress(100);
        sendProgressUpdate(consumerRecord, mapper);
    }

    private void sendProgressUpdate(ConsumerRecord consumerRecord, ObjectMapper mapper)
    {
        try
        {
            String body = mapper.writeValueAsString(consumerRecord);
            rabbitTemplate.convertAndSend(RabbitmqDemoApplication.topicExchangeName,
                    "workerResponseQueue.progressUpdate", body);
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
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
