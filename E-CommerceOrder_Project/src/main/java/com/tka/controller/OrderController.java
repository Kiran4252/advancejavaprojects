package com.tka.controller;

import java.util.List;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.tka.entity.CustomerOrder;
import com.tka.service.OrderService;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private final OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }


    // API 1
    // POST /api/orders

    @PostMapping
    public Map<String, Object> placeOrder(
            @RequestBody CustomerOrder order) {

        return orderService.placeOrder(order);
    }


  
    // API 2
    // GET /api/orders/above/{amount}

    @GetMapping("/above/{amount}")
    public List<Map<String, Object>> getOrdersAbove(
            @PathVariable double amount) {

        return orderService.getOrdersAbove(amount);
    }


 
    // API 3
    // PUT /api/orders/{orderId}/status
   
    @PutMapping("/{orderId}/status")
    public Map<String, Object> updateStatus(
            @PathVariable int orderId,
            @RequestBody Map<String, String> request) {

        String status = request.get("status");

        return orderService.updateStatus(
                orderId,
                status);
    }


    // API 4
    // GET /api/orders/summary/{category}

    @GetMapping("/summary/{category}")
    public Map<String, Object> categorySummary(
            @PathVariable String category) {

        return orderService.categorySummary(category);
    }
}