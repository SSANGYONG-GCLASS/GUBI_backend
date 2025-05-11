package com.spring.gubi.dto.carts;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UpdateCartCntRequest {
    private Integer cnt; // 수량
}
