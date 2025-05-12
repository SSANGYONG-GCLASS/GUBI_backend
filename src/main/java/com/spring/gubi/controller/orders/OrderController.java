package com.spring.gubi.controller.orders;

import com.spring.gubi.dto.orders.*;
import com.spring.gubi.service.carts.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;


    @PostMapping("/api/orders")
    public ResponseEntity<AddOrderResponse> addOrder(@RequestBody AddOrderRequest request) {
        AddOrderResponse order = orderService.saveOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }


}
