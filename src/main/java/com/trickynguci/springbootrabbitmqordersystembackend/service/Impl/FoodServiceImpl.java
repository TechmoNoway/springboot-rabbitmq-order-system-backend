package com.trickynguci.springbootrabbitmqordersystembackend.service.Impl;

import com.trickynguci.springbootrabbitmqordersystembackend.model.Food;
import com.trickynguci.springbootrabbitmqordersystembackend.repository.FoodRepository;
import com.trickynguci.springbootrabbitmqordersystembackend.service.FoodService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class FoodServiceImpl implements FoodService {

    private FoodRepository foodRepository;

    public List<Food> getAllFoods() {
        return foodRepository.findAll();
    }
}
