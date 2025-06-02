package com.spring.gubi.controller.orders;

import com.spring.gubi.dto.orders.*;
import com.spring.gubi.service.carts.OrderService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/api/orders")
    public ResponseEntity<GetOrderResponse> getOrdersByUserid(Authentication authentication, @ModelAttribute GetOrderRequest request) {
        String userId = (String) authentication.getPrincipal();
        GetOrderResponse order = orderService.getOrdersByUser_Userid(userId, request);
        return ResponseEntity.ok().body(order);
    }

    @GetMapping("/api/orders/{id}")
    public ResponseEntity<GetOneOrderResponse> getOrdersByOrderNo(Authentication authentication, @PathVariable Long id) {
        String userId = (String) authentication.getPrincipal();
        GetOneOrderResponse order = orderService.getOrdersById(userId, id);
        return ResponseEntity.ok().body(order);
    }

    @GetMapping("/api/orders/user-detail")
    public ResponseEntity<GetUserDetailForOrderResponse> getUserDetailForOrder(Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        GetUserDetailForOrderResponse userDetail = orderService.getUserDetailForOrder(userId);
        return ResponseEntity.ok().body(userDetail);
    }

    @PostMapping("/api/orders")
    public ResponseEntity<AddOrderResponse> addOrder(Authentication authentication, @RequestBody AddOrderRequest request) {
        String userId = (String) authentication.getPrincipal();
        AddOrderResponse response = orderService.saveOrder(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @PutMapping("/api/orders/{id}/status")
    public ResponseEntity<Map<String, String>> updateOrderStatus(Authentication authentication, @PathVariable Long id, @RequestBody UpdateOrderStatusRequest request) {
        String userId = (String) authentication.getPrincipal();
        orderService.updateOrderStatus(userId, id, request);
        return ResponseEntity.ok().body(Map.of("message", "주문상태 수정이 완료되었습니다."));
    }

    @PutMapping("/api/orders/{id}/delivery-date")
    public ResponseEntity<Map<String, String>> updateOrderDeliveryDate(Authentication authentication, @PathVariable Long id, @RequestBody UpdateOrderDeliveryDateRequest request) {
        String userId = (String) authentication.getPrincipal();
        orderService.updateOrderDeliveryDate(userId, id, request);
        return ResponseEntity.ok().body(Map.of("message", "배송일자 수정이 완료되었습니다."));
    }

    @DeleteMapping("/api/orders/{id}")
    public ResponseEntity<Map<String, String>> deleteOrder(Authentication authentication, @PathVariable Long id) {
        String userId = (String) authentication.getPrincipal();
        orderService.deleteOrder(userId, id);
        return ResponseEntity.ok().body(Map.of("message", "주문이 삭제되었습니다."));
    }

}
