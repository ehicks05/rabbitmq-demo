package net.ehicks.rabbitmqdemo;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.ws.rs.QueryParam;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

@EnableScheduling
@Controller
public class MainController
{
    private AmqpAdmin amqpAdmin;
    private SimpleMessageListenerContainer simpleMessageListenerContainer;
    private SimpMessagingTemplate template;
    private Runner runner;

    public MainController(AmqpAdmin amqpAdmin,
                          SimpleMessageListenerContainer simpleMessageListenerContainer,
                          SimpMessagingTemplate template, Runner runner)
    {
        this.amqpAdmin = amqpAdmin;
        this.simpleMessageListenerContainer = simpleMessageListenerContainer;
        this.template = template;
        this.runner = runner;
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

    @GetMapping("/")
    public String showIndex()
    {
        return "index";
    }

    @Scheduled(fixedRate = 33)
    public void send() throws Exception
    {
        List<ConsumerRecord> consumerProgress = new ArrayList<>(ConsumerMap.consumerMap.values());
        consumerProgress.sort(Comparator.comparing(o -> Integer.valueOf(o.getConsumerKey())));
        
        this.template.convertAndSend("/topic/messages",
                new ProgressRecord(runner.getMessagesPerSecond(), getActiveConsumers(), getQueuedMessages(), consumerProgress));
    }

    @PostMapping("/publisher")
    public String showIndex(@QueryParam("messagesPerSecond") int messagesPerSecond)
    {
        runner.setMessagesPerSecond(messagesPerSecond);
        return "redirect:/";
    }
}
