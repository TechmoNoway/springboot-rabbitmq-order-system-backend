package com.trickynguci.springbootrabbitmqordersystembackend.service;

import com.trickynguci.springbootrabbitmqordersystembackend.model.Order;

import java.util.List;

public interface OrderService {

    public Order createOrder(Order order);

    public void processOrder(Long orderId);

    public Order updateOrderStatus(Long orderId, String status);

    public List<Order> getAllOrders();

}
