package com.trickynguci.springbootrabbitmqordersystembackend.service.Impl;

import com.trickynguci.springbootrabbitmqordersystembackend.model.Order;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class OrderProducer {

    private final RabbitTemplate rabbitTemplate;

    @Value("${spring.rabbitmq.template.exchange}")
    private String exchange;

    @Value("${spring.rabbitmq.template.routing-key}")
    private String routingKey;;

    public OrderProducer(RabbitTemplate rabbitTemplate) {
        this.rabbitTemplate = rabbitTemplate;
    }

    public void sendOrderToQueue(Order order) {
        rabbitTemplate.convertAndSend(exchange, routingKey, order);
        System.out.println("Order sent to RabbitMQ: " + order);
    }
}