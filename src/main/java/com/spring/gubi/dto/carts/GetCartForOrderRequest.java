package com.spring.gubi.dto.carts;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class GetCartForOrderRequest {
    Long userNo;
    List<Long> cartNoList;
}
