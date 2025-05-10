package com.spring.gubi.controller.users;

import com.spring.gubi.domain.users.Delivery;
import com.spring.gubi.dto.users.AddDeliveryRequest;
import com.spring.gubi.dto.users.AddDeliveryResponse;
import com.spring.gubi.dto.users.UpdateDeliveryRequest;
import com.spring.gubi.dto.users.UpdateDeliveryResponse;
import com.spring.gubi.service.users.DeliveryService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.io.IOException;

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
    
    
    
}//end of class...

























