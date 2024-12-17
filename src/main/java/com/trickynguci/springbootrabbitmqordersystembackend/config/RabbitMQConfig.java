package com.trickynguci.springbootrabbitmqordersystembackend.config;

import org.springframework.amqp.core.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RabbitMQConfig {

    @Bean
    public TopicExchange restaurantExchange() {
        return new TopicExchange("restaurant.exchange");
    }

    @Bean
    public Queue orderQueue() {
        return new Queue("order.queue", true); // A generic order queue that every restaurant will listen to
    }

    // Static binding of the orderQueue to the exchange with a wildcard routing key
    @Bean
    public Binding bindingOrderQueue(Queue orderQueue, TopicExchange restaurantExchange) {
        return BindingBuilder.bind(orderQueue).to(restaurantExchange).with("order.#");
    }
}
