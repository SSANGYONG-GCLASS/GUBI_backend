package com.spring.gubi.service.users;

import com.spring.gubi.config.error.exception.DeliveryNotFoundException;
import com.spring.gubi.config.error.exception.UserNotFondException;
import com.spring.gubi.domain.users.Delivery;
import com.spring.gubi.domain.users.User;
import com.spring.gubi.dto.users.*;
import com.spring.gubi.repository.users.DeliveryRepository;
import com.spring.gubi.repository.users.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

@Service
@Slf4j
public class DeliveryService {
    
    private final DeliveryRepository deliveryRepository;
    // 임시방편! 원래는 userServiece 에서 레포지토리를 가져오는 것이 맞다!
    // private  final userService userservice; // 이게 맞음
    private final UserRepository userRepository;
    
    public DeliveryService(DeliveryRepository deliveryRepository, UserRepository userRepository) {
        this.deliveryRepository = deliveryRepository;
        this.userRepository = userRepository;
    }
    
    
    // 배송지 추가
    @Transactional
    public AddDeliveryResponse addDelivery(AddDeliveryRequest request) throws IOException {
        
        // 먼저 받은 유저 정보가 옳바른지 검사
        User user = userRepository.findById(request.getUserNo())
                        .orElseThrow(UserNotFondException::new);
        
        // 딜리버리 엔티티로 변환!
        Delivery delivery = request.toEntity(user);
        
        // 만약 옳바르면 저장, 엔티티 타입으로 다시 나온다.
        Delivery response = deliveryRepository.save(delivery);
        
        // 다시 리스폰스 타입으로 변환 후 반환
        return new AddDeliveryResponse(response);
        
    }//end of public AddDeliveryResponse addDelivery(AddDeliveryRequest request) throws IOException {}...
    
    
    
    // 배송지 수정
    @Transactional
    public UpdateDeliveryResponse updateDelivery(UpdateDeliveryRequest request) throws IOException {
        
        // 딜리버리 번호로 검색
        Delivery delivery = deliveryRepository.findById(request.getDeliveryNo())
                .orElseThrow(DeliveryNotFoundException::new);
        
        // 만약 옳바르면 바로 엔티티 정보를 교환(수정 단계)
        delivery.updateDelivery(request);
        
        return new UpdateDeliveryResponse(delivery);
        
    }//end of public UpdateDeliveryResponse updateDelivery(UpdateDeliveryRequest request) throws IOException {}...
    
    
    
    // 배송지 삭제
    @Transactional
    public void deleteDelivery(Long id) {
        
        // 딜리버리 번호로 검색
        Delivery delivery = deliveryRepository.findById(id)
                .orElseThrow(DeliveryNotFoundException::new);
        
        log.info("배송지 삭제 완료");
        
        // 검색되면 삭제
        deliveryRepository.delete(delivery);
    }//end of public void deleteDelivery(Long id) {}...
    
    
    
    
    
    
    // public GetDeliverysResponse getDelivery(GetDeliverysRequest request) {
    //
    // }
    
    
    // 배송지 삭제
    
    
    
    
    
    
    
    
}//end of class...




















