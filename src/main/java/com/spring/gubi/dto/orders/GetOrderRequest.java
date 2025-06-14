package com.spring.gubi.dto.orders;

import com.spring.gubi.domain.orders.OrderStatus;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.List;

@Getter
@Setter
public class GetOrderRequest {
    private List<OrderStatus> statuses = List.of(OrderStatus.PAYMENT_PENDING
            , OrderStatus.ORDER_COMPLETED, OrderStatus.SHIPPING, OrderStatus.DELIVERED, OrderStatus.PURCHASE_CONFIRMED
            , OrderStatus.ORDER_CANCELLED, OrderStatus.REFUND_REQUESTED, OrderStatus.REFUND_COMPLETED);
    private int page = 1;
    private int size = 10;

    public Pageable getPageable() {
        return PageRequest.of(page-1, size, Sort.Direction.DESC, "id");
    }
}
