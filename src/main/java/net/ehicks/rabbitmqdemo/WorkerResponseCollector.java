package net.ehicks.rabbitmqdemo;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Component;

import java.util.concurrent.atomic.AtomicLong;

@Component
public class WorkerResponseCollector
{
    private static AtomicLong messagesRead = new AtomicLong();
    private static long startTime = System.currentTimeMillis();

    public void receiveMessage(String message)
    {
        long reads = messagesRead.incrementAndGet();
        if (reads % 100 == 0)
        {
            double seconds = System.currentTimeMillis() - startTime;
            seconds = seconds / 1000;
            double mps = reads / seconds;
            System.out.println("Messages read by WorkerResponseCollector: " + reads + ". MPS: " + mps);
        }
        
        try
        {
            ConsumerRecord consumerRecord = new ObjectMapper().readValue(message, ConsumerRecord.class);

            if (consumerRecord.getProgress() < 100)
                ConsumerMap.consumerMap.put(consumerRecord.getConsumerKey(), consumerRecord);
            else
                ConsumerMap.consumerMap.remove((consumerRecord.getConsumerKey()));
        }
        catch (Exception e)
        {
            System.out.println(e.getMessage());
        }
    }
}

// Messages read by WorkerResponseCollector: 54000. MPS: 396.3622751194592
// after debounce:
// Messages read by WorkerResponseCollector: 13000. MPS: 81.58910471647786