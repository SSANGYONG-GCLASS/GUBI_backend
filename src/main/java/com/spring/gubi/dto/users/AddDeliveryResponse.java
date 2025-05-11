package com.spring.gubi.dto.users;

import com.spring.gubi.domain.users.Delivery;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddDeliveryResponse {
    
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
    
    
    @Override
    public String toString() {
        return "AddDeliveryResponse{" +
                "userNo=" + userNo +
                ", deliveryName='" + deliveryName + '\'' +
                ", receiver='" + receiver + '\'' +
                ", receiverTel='" + receiverTel + '\'' +
                ", zipcode='" + zipcode + '\'' +
                ", address='" + address + '\'' +
                ", detailAddress='" + detailAddress + '\'' +
                ", memo='" + memo + '\'' +
                '}';
    }
    
    // 다시 엔티티객체를 리스폰스 타입으로 변환해주는 메소드
    public AddDeliveryResponse(Delivery delivery) {
        this.userNo = delivery.getUser().getId();
        this.deliveryName = delivery.getDeliveryName();
        this.receiver = delivery.getReceiver();
        this.receiverTel = delivery.getReceiverTel();
        this.zipcode = delivery.getAddress().getZipcode();
        this.address = delivery.getAddress().getAddress();
        this.detailAddress = delivery.getAddress().getDetailAddress();
        this.memo = delivery.getMemo();
        this.isDefault = delivery.getIsDefault().toString();
    }
    
    
    
}//end of class...

