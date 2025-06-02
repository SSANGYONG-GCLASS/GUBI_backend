package com.spring.gubi.controller.orders;

import com.spring.gubi.dto.carts.*;
import com.spring.gubi.service.carts.CartService;
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
public class CartController {

    private final CartService cartService;

    @GetMapping("/api/carts")
    public ResponseEntity<GetCartResponse> getCarts(Authentication authentication) {
        String userId = (String) authentication.getPrincipal();
        GetCartResponse carts = cartService.getCartsByUser_Id(userId);
        return ResponseEntity.ok().body(carts);
    }

    @GetMapping("/api/carts/for-order")
    public ResponseEntity<GetCartForOrderResponse> getCartsForOrder(Authentication authentication, @ModelAttribute GetCartForOrderRequest request) {
        String userId = (String) authentication.getPrincipal();
        GetCartForOrderResponse carts = cartService.getCartsByIdIn(userId, request);
        return ResponseEntity.ok().body(carts);
    }

    @PostMapping("/api/carts")
    public ResponseEntity<Map<String, String>> addCart(Authentication authentication, @RequestBody AddCartRequest request) {
        String userId = (String) authentication.getPrincipal();
        cartService.saveCart(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "장바구니에 추가되었습니다."));
    }

    @PutMapping("/api/carts/{id}")
    public ResponseEntity<Map<String, String>> updateCart(Authentication authentication, @PathVariable Long id, @RequestBody UpdateCartCntRequest request) {
        String userId = (String) authentication.getPrincipal();
        cartService.updateCart(userId, id, request);
        return ResponseEntity.ok().body(Map.of("message", "장바구니가 수정되었습니다."));
    }

    @DeleteMapping("/api/carts/{id}")
    public ResponseEntity<Map<String, String>> deleteCart(Authentication authentication, @PathVariable Long id) {
        String userId = (String) authentication.getPrincipal();
        cartService.deleteCart(userId, id);
        return ResponseEntity.ok().body(Map.of("message", "장바구니가 삭제되었습니다."));
    }

}
