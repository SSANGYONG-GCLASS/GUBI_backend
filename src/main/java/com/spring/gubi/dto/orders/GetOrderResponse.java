package com.spring.gubi.dto.orders;

import com.spring.gubi.domain.orders.Order;
import com.spring.gubi.util.Pagination;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
public class GetOrderResponse {
    List<GetOrderDTO> orders;
    Pagination pagination;

    public GetOrderResponse(Page<Order> orders, Pagination pagination) {
        this.orders = orders.getContent().stream().map(GetOrderDTO::new).toList();
        this.pagination = pagination;
    }
}
