package com.spring.gubi.dto.users;

import com.spring.gubi.domain.users.Address;
import com.spring.gubi.domain.users.Delivery;
import com.spring.gubi.domain.users.DeliveryDefault;
import com.spring.gubi.domain.users.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddDeliveryRequest {
    
    // 수령인 유저 번호
    private Long userNo;
    
    // 배송지명
    private String deliveryName;
    
    // 수령인
    private String receiver;
    
    // 수령인 전화번호
    private String receiverTel;
    
    // 주소(우편번호, 주소, 상세주소)
    private String zipcode;
    private String address;
    private String detailAddress;
    
    
    // 배송 시 요청사항
    private String memo;
    
    // 기본배송지 여부 (ENUM: DEFAULT, NONE)
    private String isDefault;
    
    public String toString() {
        return "AddDeliveryRequest{" +
                "deliveryName='" + deliveryName + '\'' +
                ", receiver='" + receiver + '\'' +
                ", receiverTel='" + receiverTel + '\'' +
                ", address=" + address +
                ", memo='" + memo + '\'' +
                ", isDefault=" + isDefault +
                '}';
    }
    
    
    // 엔티티 User를 전달받아 Delivery 엔티티 객체를 만드는 메소드
    public Delivery toEntity(User user) { // 검색된 user 타입의 user 를 받아서 사용!!
        // 먼저 Address 객체 만들어줌
        Address address1 = new Address(zipcode, address, detailAddress);
        
        return Delivery.builder()
                .user(user)
                .deliveryName(deliveryName)
                .receiver(receiver)
                .receiverTel(receiverTel)
                .address(address1)
                .memo(memo)
                .isDefault(DeliveryDefault.valueOf(isDefault)).build(); // 이러면 enum 타입으로 들어간다!
    }
    
    
}//end of class...
