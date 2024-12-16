package com.trickynguci.springbootrabbitmqordersystembackend.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.sql.Timestamp;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Order {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long foodId;

    @Column(nullable = false)
    private int quantity;

    @Column(nullable = false)
    private String status; // PENDING, IN_PROGRESS, COMPLETED

    @Column(nullable = false)
    private Timestamp createdAt;

    @Column(nullable = true)
    private Timestamp completedAt;
}
