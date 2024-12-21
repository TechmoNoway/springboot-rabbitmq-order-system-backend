package com.trickynguci.springbootrabbitmqordersystembackend.service.Impl;

import com.trickynguci.springbootrabbitmqordersystembackend.model.Order;
import com.trickynguci.springbootrabbitmqordersystembackend.model.Restaurant;
import com.trickynguci.springbootrabbitmqordersystembackend.repository.OrderRepository;
import com.trickynguci.springbootrabbitmqordersystembackend.repository.RestaurantRepository;
import com.trickynguci.springbootrabbitmqordersystembackend.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@RequiredArgsConstructor
@Service
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final RestaurantRepository restaurantRepository;

    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public Order createOrder(Order order) {
        order.setStatus("PENDING");
        order.setCreatedAt(LocalDateTime.now());
        order.setCompletedAt(LocalDateTime.now());
        return orderRepository.save(order);
    }

    @Override
    public void processOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        // Assign order to a restaurant
        List<Restaurant> availableRestaurants = restaurantRepository.findByIsAvailableTrue();
        if (availableRestaurants.isEmpty()) {
            throw new RuntimeException("No available restaurants");
        }

        assignOrderToRestaurant(order);

        Restaurant assignedRestaurant = availableRestaurants.get(0);
        order.setRestaurantId(assignedRestaurant.getId());
        order.setStatus("IN_PROGRESS");
        orderRepository.save(order);

        // Notify via WebSocket
        messagingTemplate.convertAndSend("/topic/orders", order);
    }

    public Order updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        order.setStatus(status);

        if ("COMPLETED".equals(status)) {
            order.setCompletedAt(LocalDateTime.now());
            Restaurant restaurant = restaurantRepository.findById(order.getRestaurantId()).orElse(null);
            if (restaurant != null) {
                restaurant.getCurrentOrders().decrementAndGet();
            }
        }

        return orderRepository.save(order);
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    private void assignOrderToRestaurant(Order order) {
        List<Restaurant> availableRestaurants = restaurantRepository.findByIsAvailableTrue();
        if (availableRestaurants.isEmpty()) {
            throw new RuntimeException("No available restaurants");
        }

        Restaurant assignedRestaurant = availableRestaurants.get(0);
        order.setRestaurantId(assignedRestaurant.getId());
        order.setStatus("IN_PROGRESS");
        orderRepository.save(order);

        messagingTemplate.convertAndSend("/topic/orders", order);
    }

    public Order reassignOrder(Long orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));

        Restaurant currentRestaurant = restaurantRepository.findById(order.getRestaurantId()).orElse(null);
        if (currentRestaurant == null) {
            throw new RuntimeException("Order is not assigned to any restaurant");
        }

        // Tìm nhà hàng khác
        List<Restaurant> availableRestaurants = restaurantRepository.findByIsAvailableTrue();
        availableRestaurants.remove(currentRestaurant); // Loại nhà hàng hiện tại ra khỏi danh sách

        if (availableRestaurants.isEmpty()) {
            throw new RuntimeException("No other available restaurants");
        }

        Restaurant newRestaurant = availableRestaurants.get(0);
        order.setRestaurantId(newRestaurant.getId());
        orderRepository.save(order);

        // Notify via WebSocket
        messagingTemplate.convertAndSend("/topic/orders", order);

        return order;
    }
}
