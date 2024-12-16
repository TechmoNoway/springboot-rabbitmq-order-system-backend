package com.trickynguci.springbootrabbitmqordersystembackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.concurrent.atomic.AtomicInteger;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Restaurant {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private String location;

    @Column(nullable = false)
    private boolean isAvailable;

    @Column(nullable = false)
    private int maxOrders; // Số lượng đơn tối đa mà nhà hàng có thể xử lý

    @Transient
    private AtomicInteger currentOrders = new AtomicInteger(0); // Số đơn hiện tại

    public boolean isBusy() {
        return currentOrders.get() >= maxOrders;
    }
}