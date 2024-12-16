package com.trickynguci.springbootrabbitmqordersystembackend.config;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.amqp.rabbit.listener.MessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.support.converter.Jackson2JsonMessageConverter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitConfig {

    // Declare a queue
    @Bean
    public Queue orderQueue() {
        return new Queue("orderQueue", true); // durable queue
    }

    // Declare an exchange
    @Bean
    public TopicExchange exchange() {
        return new TopicExchange("orderExchange");
    }

    // Bind the queue to the exchange
    @Bean
    public Binding binding(Queue orderQueue, TopicExchange exchange) {
        return BindingBuilder.bind(orderQueue).to(exchange).with("order.#");
    }

    // RabbitTemplate configuration
    @Bean
    public RabbitTemplate rabbitTemplate(ConnectionFactory connectionFactory) {
        RabbitTemplate rabbitTemplate = new RabbitTemplate(connectionFactory);
        rabbitTemplate.setMessageConverter(new Jackson2JsonMessageConverter());
        return rabbitTemplate;
    }

    // Listener container to listen for messages
    @Bean
    public MessageListenerContainer messageListenerContainer(ConnectionFactory connectionFactory, MessageListener messageListener) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(connectionFactory);
        container.setQueues(orderQueue());
        container.setMessageListener(messageListener);
        return container;
    }

    // Define a listener for messages
    @Bean
    public MessageListener messageListener() {
        return message -> {
            String messageBody = new String(message.getBody());
            System.out.println("Received message: " + messageBody);
        };
    }

}
