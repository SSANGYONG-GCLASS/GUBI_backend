package com.spring.gubi.dto.orders;

import com.fasterxml.jackson.annotation.JsonSetter;
import com.fasterxml.jackson.annotation.Nulls;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class UpdateOrderDeliveryDateRequest {
    private LocalDateTime deliveryDate = LocalDateTime.now(); // 배송완료일자
}
