package com.spring.gubi.dto.orders;

import com.spring.gubi.domain.orders.Order;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class GetOneOrderResponse {

    private GetOneOrderDTO order;

    public GetOneOrderResponse(Order order) {
        this.order = new GetOneOrderDTO(order);
    }
}
