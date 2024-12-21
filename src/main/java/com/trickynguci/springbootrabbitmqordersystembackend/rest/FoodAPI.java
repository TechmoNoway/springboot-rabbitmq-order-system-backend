package com.trickynguci.springbootrabbitmqordersystembackend.rest;

import com.trickynguci.springbootrabbitmqordersystembackend.service.FoodService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/foods")
public class FoodAPI {

    private final FoodService foodService;

    public ResponseEntity<?> getAllFoods() {

        Map<String, Object> result = new HashMap<>();

        try {
            result.put("success", true);
            result.put("message", "Order created successfully");
            result.put("data", foodService.getAllFoods());
        } catch (Exception e) {
            result.put("success", false);
            result.put("message", "Order creation failed");
            result.put("data", null);
            log.error("Error: ", e);
        }
    
        return ResponseEntity.ok(result);
    }
}
