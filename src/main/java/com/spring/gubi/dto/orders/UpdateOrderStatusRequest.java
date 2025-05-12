package com.spring.gubi.dto.orders;

import com.spring.gubi.domain.orders.OrderStatus;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateOrderStatusRequest {
    private OrderStatus status; // 주문상태 (ENUM: 결제대기... 환불완료)
}
