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

    @GetMapping("/api/orders")
    public ResponseEntity<GetOrderResponse> getOrdersByUserid(@ModelAttribute GetOrderRequest request) {
        GetOrderResponse order = orderService.getOrdersByUser_Id(request);
        return ResponseEntity.ok().body(order);
    }

    @PostMapping("/api/orders")
    public ResponseEntity<AddOrderResponse> addOrder(@RequestBody AddOrderRequest request) {
        AddOrderResponse order = orderService.saveOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(order);
    }

    @PutMapping("/api/orders/{id}/status")
    public ResponseEntity<Map<String, String>> updateOrderStatus(@PathVariable Long id, @RequestBody UpdateOrderStatusRequest request) {
        orderService.updateOrderStatus(id, request);
        return ResponseEntity.ok().body(Map.of("message", "주문상태 수정이 완료되었습니다."));
    }

    @PutMapping("/api/orders/{id}/delivery-date")
    public ResponseEntity<Map<String, String>> updateOrderDeliveryDate(@PathVariable Long id, @RequestBody UpdateOrderDeliveryDateRequest request) {
        orderService.updateOrderDeliveryDate(id, request);
        return ResponseEntity.ok().body(Map.of("message", "배송일자 수정이 완료되었습니다."));
    }

}
