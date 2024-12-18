package com.trickynguci.springbootrabbitmqordersystembackend.config;

import com.trickynguci.springbootrabbitmqordersystembackend.service.Impl.OrderConsumer;
import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitListenerConfig {

    private final OrderConsumer orderConsumer;

    public RabbitListenerConfig(OrderConsumer orderConsumer) {
        this.orderConsumer = orderConsumer;
    }

    @Bean
    public MessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);

        // Get the list of restaurant IDs dynamically or statically
        // For example, let's assume restaurant IDs are "1", "2", and "3"
        String[] restaurantIds = {"1", "2", "3"};

        // Declare listeners for each restaurant queue
        for (String restaurantId : restaurantIds) {
            String queueName = "restaurant.queue." + restaurantId;
            container.addQueueNames(queueName);
        }

        container.setMessageListener(message -> {
            // Process the message here, the restaurant ID can be derived from the queue name
            String queueName = message.getMessageProperties().getConsumerQueue();
            String restaurantId = queueName.replace("restaurant.queue.", "");
            orderConsumer.processOrder(message.getBody().toString(), restaurantId);
        });

        return container;
    }
}