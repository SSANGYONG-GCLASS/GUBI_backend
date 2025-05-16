package com.spring.gubi.dto.orders;

import com.spring.gubi.domain.orders.Order;
import com.spring.gubi.domain.orders.OrderStatus;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class AddOrderDTO {
    private Long id; // 주문 번호 (시퀀스 자동 증가)
    private Long totalPrice; // 총 주문 금액
    private Integer usePoint; // 포인트 사용금액
    private Integer rewardPoint; // 포인트 적립금
    private Integer deliveryPrice; // 배송비
    private Integer totalCnt; // 총 수량
    private LocalDateTime orderDay; // 주문 일자
    private OrderStatus status; // 주문상태 (ENUM: 결제대기... 환불완료)
    private LocalDateTime deliveryDate; // 배송완료일자

    // 배송지
    private AddOrderDeliveryDTO delivery;

    // 주문 상세 목록
    private List<AddOrderDetailDTO> orderDetails;

    public AddOrderDTO(Order order) {
        this.id = order.getId();
        this.totalPrice = order.getTotalPrice();
        this.usePoint = order.getUsePoint();
        this.rewardPoint = order.getRewardPoint();
        this.deliveryPrice = order.getDeliveryPrice();
        this.totalCnt = order.getTotalCnt();
        this.orderDay = order.getOrderday();
        this.status = order.getStatus();
        this.deliveryDate = order.getDeliveryDate();

        this.delivery = new AddOrderDeliveryDTO(order.getDelivery());

        this.orderDetails = order.getOrderDetails().stream().map(AddOrderDetailDTO::new).collect(Collectors.toList());
    }
}
