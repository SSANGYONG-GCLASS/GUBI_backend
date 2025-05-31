package com.spring.gubi.dto.users;


import com.spring.gubi.domain.users.Delivery;
import lombok.Getter;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Getter
@Setter
public class GetDeliveryDTO {
    
    private Long userNo;            //
    private Long deliveryNo;
    private String deliveryName;
    private String receiver;
    private String receiverTel;
    
    private String zipcode;
    private String address;
    private String detailAddress;
    
    // 배송 시 요청사항
    private String memo;
    
    // 기본배송지 여부 (ENUM: DEFAULT, NONE)
    private String isDefault;
    
    
    // 생성자 (딜리버리 엔티티를 넣으면 DTO로 변환)
    public GetDeliveryDTO(Delivery delivery) {
        this.userNo = delivery.getUser().getId();
        this.deliveryNo = delivery.getId();
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





