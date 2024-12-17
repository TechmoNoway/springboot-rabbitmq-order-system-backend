package com.trickynguci.springbootrabbitmqordersystembackend.config;

import org.springframework.amqp.rabbit.annotation.EnableRabbit;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@EnableRabbit
public class RabbitListenerConfig {

    private final RabbitTemplate rabbitTemplate;

    public RabbitListenerConfig(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    @Bean
    public MessageListenerContainer messageListenerContainer() {
        // Dynamically create listener container for queues
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(rabbitTemplate.getConnectionFactory());
        container.setQueueNames("restaurant.queue.*");  // Replace with your dynamic queue name logic
        container.setMessageListener((message) -> {
            String restaurantId = extractRestaurantIdFromQueueName(message.getMessageProperties().getConsumerQueue());
            // Process the message dynamically based on the queue (restaurant ID)
            rabbitTemplate.convertAndSend("restaurant.queue." + restaurantId, message.getBody());
        });
        return container;
    }

    // Utility method to extract restaurant ID from the queue name
    private String extractRestaurantIdFromQueueName(String queueName) {
        return queueName.replace("restaurant.queue.", "");
    }
}