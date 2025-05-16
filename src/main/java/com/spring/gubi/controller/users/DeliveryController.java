package com.spring.gubi.controller.users;

import com.spring.gubi.dto.users.*;
import com.spring.gubi.service.users.DeliveryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;
import java.util.Map;

@RestController
@Slf4j
public class DeliveryController {

    
    private final DeliveryService deliveryService;
    // 생성자 주입!
    public DeliveryController(DeliveryService deliveryService) {
        this.deliveryService = deliveryService;
    }
    
    
    // 배송지 추가!
    @PostMapping(value = "/api/delivery")
    public ResponseEntity<AddDeliveryResponse> addDelivery(@RequestBody AddDeliveryRequest request) throws IOException {
        
        AddDeliveryResponse response = deliveryService.addDelivery(request);
        
        log.info("등록한 배송지 정보: {}", response);
        
        return ResponseEntity.status(HttpStatus.OK).body(response);
        
    }//end of public ResponseEntity<AddDeliveryResponse> addDelivery(@RequestBody AddDeliveryRequest request) {S}...
    
    
    
    // 배송지 수정
    @PutMapping(value = "/api/delivery")
    public ResponseEntity<UpdateDeliveryResponse> updateDelivery(@RequestBody UpdateDeliveryRequest request) throws IOException {
        
        UpdateDeliveryResponse response = deliveryService.updateDelivery(request);
        
        log.info("수정한 배송지 정보: {}", response);
        
        return ResponseEntity.status(HttpStatus.OK).body(response);
        
    }//end of public ResponseEntity<AddDeliveryResponse> updateDelivery(@RequestBody AddDeliveryRequest request) throws IOException {}...
    
    
    // 배송지 삭제
    @DeleteMapping(value = "/api/delivery/{id}")
    public  ResponseEntity<Map<String, String>> deleteDelivery(@PathVariable("id") Long id) throws IOException {
        System.out.println("아이디=> "+id);
        deliveryService.deleteDelivery(id);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("message","배송지가 삭제되었습니다."));
    }//end of public  ResponseEntity<Map<String, String>> deleteDelivery(@PathVariable("id") Long id) throws IOException {}...
    
    
    
    // // 배송지 목록 조회
    // @GetMapping(value = "/api/delivery")
    // public ResponseEntity<GetDeliverysResponse> getDelivery(@ModelAttribute GetDeliverysRequest request) throws IOException {
    //
    //     GetDeliverysResponse delivery = deliveryService.getDelivery(request);
    //     return ResponseEntity.status(HttpStatus.OK).body(delivery);
    // }//end of
    
    
}//end of class...

























