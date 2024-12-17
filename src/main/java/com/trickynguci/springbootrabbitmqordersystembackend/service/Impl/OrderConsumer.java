package com.trickynguci.springbootrabbitmqordersystembackend.service.Impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.trickynguci.springbootrabbitmqordersystembackend.model.Order;
import com.trickynguci.springbootrabbitmqordersystembackend.model.Restaurant;
import com.trickynguci.springbootrabbitmqordersystembackend.repository.RestaurantRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
public class OrderConsumer {

    private final RestaurantRepository restaurantRepository;
    private final RabbitTemplate rabbitTemplate;
    private final ObjectMapper objectMapper;

    @Value("${spring.rabbitmq.restaurantQueuePrefix}")
    private String restaurantQueuePrefix;

    // Method for processing orders
    public void processOrder(String message) {
        try {
            // Convert json message to Order object
            Order order = objectMapper.readValue(message, Order.class);
            System.out.println("Processing order: " + order);

            // Check the current restaurant status
            Restaurant assignedRestaurant = restaurantRepository.findById(order.getRestaurant().getId())
                    .orElseThrow(() -> new RuntimeException("Restaurant not found"));

            if (assignedRestaurant.isBusy()) {
                // If this restaurant is busy, reassign the order to another restaurant
                List<Restaurant> availableRestaurants = restaurantRepository.findByIsAvailableTrue();
                availableRestaurants.remove(assignedRestaurant);

                if (!availableRestaurants.isEmpty()) {
                    Restaurant newRestaurant = availableRestaurants.get(0);
                    order.setRestaurant(newRestaurant);
                    System.out.println("Reassigning order to restaurant: " + newRestaurant.getName());

                    // Send the order to the new restaurant
                    rabbitTemplate.convertAndSend("restaurant.queue." + newRestaurant.getId(), objectMapper.writeValueAsString(order));
                } else {
                    System.out.println("No available restaurants to process the order.");
                }
            } else {
                // Restaurant is available, send the order to the restaurant
                System.out.println("Sending order to restaurant: " + assignedRestaurant.getName());
                rabbitTemplate.convertAndSend("restaurant.queue." + assignedRestaurant.getId(), message);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // Bean for dynamic queue name based on restaurant id
    public String getRestaurantQueueName(String restaurantId) {
        return "restaurant.queue." + restaurantId;
    }
}
