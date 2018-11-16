package net.ehicks.rabbitmqdemo;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class RabbitmqDemoApplication
{
    static final String topicExchangeName = "spring-boot-exchange";
    static final String queueName = "spring-boot";

    public static void main(String[] args)
    {
        SpringApplication.run(RabbitmqDemoApplication.class, args);
    }

    @Bean Queue queue() {
        return new Queue(queueName, false);
    }

    @Bean TopicExchange exchange() {
        return new TopicExchange(topicExchangeName);
    }

    @Bean Binding binding(Queue queue, TopicExchange exchange) {
        return BindingBuilder.bind(queue).to(exchange).with("workQueue.#");
    }

    @Bean
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
                                             MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setPrefetchCount(1);
        container.setQueueNames(queueName);
        container.setMessageListener(listenerAdapter);
        container.setConcurrency("1-256");
        return container;
    }

    @Bean MessageListenerAdapter listenerAdapter(Worker worker) {
        return new MessageListenerAdapter(worker, "receiveMessage");
    }
}
