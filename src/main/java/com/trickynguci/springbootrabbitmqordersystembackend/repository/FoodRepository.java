package com.trickynguci.springbootrabbitmqordersystembackend.repository;

import com.trickynguci.springbootrabbitmqordersystembackend.model.Food;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FoodRepository extends JpaRepository<Food, Long> {

}
