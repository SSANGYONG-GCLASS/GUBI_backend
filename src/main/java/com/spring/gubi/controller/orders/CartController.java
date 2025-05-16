package com.spring.gubi.controller.orders;

import com.spring.gubi.dto.carts.AddCartRequest;
import com.spring.gubi.dto.carts.GetCartRequest;
import com.spring.gubi.dto.carts.GetCartResponse;
import com.spring.gubi.dto.carts.UpdateCartCntRequest;
import com.spring.gubi.service.carts.CartService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@Slf4j
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("/api/carts")
    public ResponseEntity<GetCartResponse> getCarts(@ModelAttribute GetCartRequest request) {
        GetCartResponse carts = cartService.getCartsByUser_Id(request);
        return ResponseEntity.ok().body(carts);
    }

    @PostMapping("/api/carts")
    public ResponseEntity<Map<String, String>> addCart(@RequestBody AddCartRequest request) {
        cartService.saveCart(request);
        return ResponseEntity.status(HttpStatus.CREATED).body(Map.of("message", "장바구니에 추가되었습니다."));
    }

    @PutMapping("/api/carts/{id}")
    public ResponseEntity<Map<String, String>> updateCart(@PathVariable Long id, @RequestBody UpdateCartCntRequest request) {
        cartService.updateCart(id, request);
        return ResponseEntity.ok().body(Map.of("message", "장바구니가 수정되었습니다."));
    }

    @DeleteMapping("/api/carts/{id}")
    public ResponseEntity<Map<String, String>> deleteCart(@PathVariable Long id) {
        cartService.deleteCart(id);
        return ResponseEntity.ok().body(Map.of("message", "장바구니가 삭제되었습니다."));
    }

}
