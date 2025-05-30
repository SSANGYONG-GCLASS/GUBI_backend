package com.spring.gubi.dto.orders;

import com.spring.gubi.domain.orders.Order;
import lombok.Getter;

@Getter
public class AddOrderResponse {
    Long orderNo;

    public AddOrderResponse(Order order) {
        this.orderNo = order.getId();
    }
}
