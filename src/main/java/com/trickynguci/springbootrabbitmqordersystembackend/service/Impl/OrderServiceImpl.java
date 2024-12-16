package com.trickynguci.springbootrabbitmqordersystembackend.service.Impl;

import com.trickynguci.springbootrabbitmqordersystembackend.model.Order;
import com.trickynguci.springbootrabbitmqordersystembackend.repository.OrderRepository;
import com.trickynguci.springbootrabbitmqordersystembackend.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;

    private final OrderProducer orderProducer;

    public Order createOrder(Long foodId, int quantity) {
        Order order = new Order();
        order.setFoodId(foodId);
        order.setQuantity(quantity);
        order.setStatus("PENDING");
        order.setCreatedAt(Timestamp.valueOf(LocalDateTime.now()));

        Order savedOrder = orderRepository.save(order);

        orderProducer.sendOrderToQueue(savedOrder);

        return savedOrder;
    }

    @Override
    public List<Order> getOrdersByStatus(String status) {
        return orderRepository.findByStatus(status);
    }

    @Override
    public Order updateOrderStatus(Long orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found"));
        order.setStatus(status);
        if (status.equals("COMPLETED")) {
            order.setCompletedAt(Timestamp.valueOf(LocalDateTime.now()));
        }
        return orderRepository.save(order);
    }
}
