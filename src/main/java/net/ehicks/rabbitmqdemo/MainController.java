package net.ehicks.rabbitmqdemo;

import org.springframework.amqp.core.AmqpAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import javax.ws.rs.QueryParam;

@EnableScheduling
@Controller
public class MainController
{
    private AmqpAdmin amqpAdmin;
    private SimpleMessageListenerContainer simpleMessageListenerContainer;
    private SimpMessagingTemplate template;
    private Runner runner;

    public MainController(AmqpAdmin amqpAdmin,
                          @Qualifier("container")SimpleMessageListenerContainer simpleMessageListenerContainer,
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
    public String showIndex(Model model)
    {
        model.addAttribute("messagesPerSecond", runner.getMessagesPerSecond());
        return "index";
    }

    @Scheduled(fixedRate = 33)
    public void send()
    {
        this.template.convertAndSend("/topic/messages",
                new ProgressRecord(runner.getMessagesPerSecond(), getActiveConsumers(), getQueuedMessages(), ConsumerMap.consumerMap));
    }

    @PostMapping("/publisher")
    public String updateSettings(@QueryParam("messagesPerSecond") int messagesPerSecond)
    {
        runner.setMessagesPerSecond(messagesPerSecond);
        return "redirect:/";
    }
}
