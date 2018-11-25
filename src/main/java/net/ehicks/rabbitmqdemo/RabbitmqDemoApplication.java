package net.ehicks.rabbitmqdemo;

import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.BindingBuilder;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.core.TopicExchange;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.adapter.MessageListenerAdapter;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

@SpringBootApplication
public class RabbitmqDemoApplication
{
    static final String topicExchangeName = "spring-boot-exchange";
    static final String queueName = "spring-boot";
    static final String workerResponseQueueName = "worker-response-queue";

    public static void main(String[] args)
    {
        SpringApplication.run(RabbitmqDemoApplication.class, args);
    }

    @Bean Queue queue() {
        return new Queue(queueName, false);
    }

    @Bean Queue workerResponseQueue() {
        return new Queue(workerResponseQueueName, false);
    }

    @Bean TopicExchange exchange() {
        return new TopicExchange(topicExchangeName);
    }

    @Bean Binding binding(TopicExchange exchange) {
        return BindingBuilder.bind(queue()).to(exchange).with("workQueue.#");
    }

    @Bean Binding workerResponseBinding(TopicExchange exchange) {
        return BindingBuilder.bind(workerResponseQueue()).to(exchange).with("workerResponseQueue.#");
    }

    @Bean @Primary @Qualifier("container")
    SimpleMessageListenerContainer container(ConnectionFactory connectionFactory,
                                             @Qualifier("listenerAdapter") MessageListenerAdapter listenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setPrefetchCount(1);
        container.setQueueNames(queueName);
        container.setMessageListener(listenerAdapter);
        container.setConcurrency("1-256");
        container.setStartConsumerMinInterval(100);
        container.setStopConsumerMinInterval(100);
        container.setConsecutiveActiveTrigger(2);
        container.setConsecutiveIdleTrigger(2);
        return container;
    }

    @Bean @Qualifier("workerResponseContainer")
    SimpleMessageListenerContainer workerResponseContainer(ConnectionFactory connectionFactory,
                                             @Qualifier("workerResponseListenerAdapter") MessageListenerAdapter workerResponseListenerAdapter) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueueNames(workerResponseQueueName);
        container.setMessageListener(workerResponseListenerAdapter);
        container.setMaxConcurrentConsumers(1);
        return container;
    }

    @Bean MessageListenerAdapter listenerAdapter(Worker worker) {
        return new MessageListenerAdapter(worker, "receiveMessage");
    }

    @Bean MessageListenerAdapter workerResponseListenerAdapter(WorkerResponseCollector workerResponseCollector) {
        return new MessageListenerAdapter(workerResponseCollector, "receiveMessage");
    }
}
