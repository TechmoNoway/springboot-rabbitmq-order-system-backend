package com.trickynguci.springbootrabbitmqordersystembackend.repository;

import com.trickynguci.springbootrabbitmqordersystembackend.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    List<Order> findByStatus(String status);

}
