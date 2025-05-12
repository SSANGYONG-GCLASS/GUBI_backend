package com.spring.gubi.dto.orders;

import com.spring.gubi.domain.orders.Order;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AddOrderResponse {

    private AddOrderDTO order;

    public AddOrderResponse(Order order) {
        this.order = new AddOrderDTO(order);
    }
}
