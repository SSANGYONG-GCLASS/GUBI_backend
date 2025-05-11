package com.spring.gubi.dto.orders;

import com.spring.gubi.domain.users.Address;
import com.spring.gubi.domain.users.Delivery;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddOrderDeliveryDTO {
    // 배송지
    private Long id; // 배송지 번호 (FK)
    private String deliveryName; // 배송지명
    private String receiver; // 수령인
    private String receiverTel; // 수령인 전화번호
    private Address address; // 주소클래스의 컬럼을 모두 가져옴(우편번호, 주소, 상세주소)
    private String memo; // 배송 시 요청사항

    public AddOrderDeliveryDTO(Delivery delivery) {
        this.id = delivery.getId();
        this.deliveryName = delivery.getDeliveryName();
        this.receiver = delivery.getReceiver();
        this.receiverTel = delivery.getReceiverTel();
        this.address = delivery.getAddress();
        this.memo = delivery.getMemo();
    }
}
