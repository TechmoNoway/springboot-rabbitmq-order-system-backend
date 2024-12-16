package com.trickynguci.springbootrabbitmqordersystembackend.rest;

import com.trickynguci.springbootrabbitmqordersystembackend.model.Order;
import com.trickynguci.springbootrabbitmqordersystembackend.service.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@Slf4j
@RestController
@RequestMapping("/api/orders")
public class OrderAPI {

    private final OrderService orderService;

    @PostMapping("/createOrder")
    public ResponseEntity<?> doCreateOrder(@RequestBody Order order) {
        Map<String, Object> result = new HashMap<>();

        try {
            result.put("success", true);
            result.put("message", "Order created successfully");
            result.put("data", orderService.createOrder(order));
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Order creation failed");
            result.put("data", null);
            log.error("Error: ", e);
        }

        return ResponseEntity.ok(result);
    }

//    @GetMapping("/getOrderByStatus")
//    public ResponseEntity<?> doGetOrdersByStatus(@RequestParam String status) {
//        Map<String, Object> result = new HashMap<>();
//
//        try {
//            result.put("success", true);
//            result.put("message", "Call doGetOrdersByStatus api successfully");
//            result.put("data", orderService.getOrdersByStatus(status));
//        } catch (Exception e) {
//            result.put("success", false);
//            result.put("message", "Call doGetOrderByStatus api failed");
//            result.put("data", null);
//            log.error("Error: ", e);
//        }
//
//        return ResponseEntity.ok(result);
//    }

    @PutMapping("/{id}/status")
    public ResponseEntity<?> doUpdateOrderStatus(@PathVariable Long id, @RequestParam String status) {
        Map<String, Object> result = new HashMap<>();

        try {
            result.put("success", true);
            result.put("message", "Call doUpdateOrderStatus api successfully");
            result.put("data", orderService.updateOrderStatus(id, status));
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Call doUpdateOrderStatus api failed");
            result.put("data", null);
            log.error("Error: ", e);
        }

        return ResponseEntity.ok(result);
    }

    @GetMapping("/getAllOrders")
    public ResponseEntity<?> doGetAllOrders() {
        Map<String, Object> result = new HashMap<>();

        try {
            result.put("success", true);
            result.put("message", "Call doGetAllOrders api successfully");
            result.put("data", orderService.getAllOrders());
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Call doGetAllOrders api failed");
            result.put("data", null);
            log.error("Error: ", e);
        }

        return ResponseEntity.ok(result);
    }
}
