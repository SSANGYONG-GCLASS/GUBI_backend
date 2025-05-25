package com.spring.gubi.dto.carts;

import com.spring.gubi.domain.carts.Cart;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class GetCartForOrderResponse {
    List<GetCartDTO> carts;

    public GetCartForOrderResponse(List<Cart> carts) {
        this.carts = carts.stream().map(GetCartDTO::new).collect(Collectors.toList());
    }
}
