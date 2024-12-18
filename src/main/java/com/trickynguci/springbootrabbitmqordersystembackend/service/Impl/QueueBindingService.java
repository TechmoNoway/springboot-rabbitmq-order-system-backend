package com.trickynguci.springbootrabbitmqordersystembackend.service.Impl;

import com.trickynguci.springbootrabbitmqordersystembackend.model.Restaurant;
import com.trickynguci.springbootrabbitmqordersystembackend.repository.RestaurantRepository;
import jakarta.annotation.PostConstruct;
import org.springframework.amqp.core.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class QueueBindingService {

    @Autowired
    private AmqpAdmin amqpAdmin;

    @Autowired
    private RestaurantRepository restaurantRepository;

    @Autowired
    private TopicExchange restaurantExchange;

    @PostConstruct
    public void initializeRestaurantQueues() {
        List<Restaurant> restaurants = restaurantRepository.findAll();

        if (restaurants.isEmpty()) {
            System.out.println("No restaurants found. No queues will be created.");
            return; // No restaurants, no queues to create.
        }

        for (Restaurant restaurant : restaurants) {
            if (restaurant.getId() == null || restaurant.getId() <= 0) {
                continue; // Skip invalid restaurant
            }

            String queueName = "restaurant.queue." + restaurant.getId();
            Queue queue = new Queue(queueName, true);
            amqpAdmin.declareQueue(queue);

            // Create a binding for the restaurant-specific queue to the exchange with a routing key
            String routingKey = "restaurant." + restaurant.getId();
            amqpAdmin.declareBinding(
                    BindingBuilder.bind(queue).to(restaurantExchange).with(routingKey)
            );

            System.out.println("Queue created and bound: " + queueName + " with routing key: " + routingKey);
        }
    }
}
