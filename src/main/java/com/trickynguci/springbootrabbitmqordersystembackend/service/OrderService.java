package com.trickynguci.springbootrabbitmqordersystembackend.service;

import com.trickynguci.springbootrabbitmqordersystembackend.model.Order;

import java.util.List;

public interface OrderService {

    public Order createOrder(Long foodId, int quantity);

    public List<Order> getOrdersByStatus(String status);

    public Order updateOrderStatus(Long orderId, String status);

}
