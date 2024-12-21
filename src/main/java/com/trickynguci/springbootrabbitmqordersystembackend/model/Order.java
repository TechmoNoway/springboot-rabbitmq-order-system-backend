package com.trickynguci.springbootrabbitmqordersystembackend.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "orders")
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long foodId;

    private Long quantity;

    @Column(nullable = false)
    private String customerName;

    @Column(nullable = false)
    private String customerAddress;

    @Column(nullable = false)
    private String status; // PENDING, IN_PROGRESS, COMPLETED

    private LocalDateTime createdAt;

    private LocalDateTime completedAt;

    private Long restaurantId;
}
