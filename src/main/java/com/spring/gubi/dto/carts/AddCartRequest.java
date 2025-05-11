package com.spring.gubi.dto.carts;

import com.spring.gubi.domain.carts.Cart;
import com.spring.gubi.domain.product.Option;
import com.spring.gubi.domain.users.User;
import lombok.*;

@Getter
@Setter
@Builder
public class AddCartRequest {
    private Long userNo; // 회원 번호 (FK)
    private Long optionNo; // 옵션 번호 (FK)
    private Integer cnt; // 수량

    public Cart toEntity(User user, Option option) {
        return Cart.builder()
                .user(user)
                .option(option)
                .cnt(cnt)
                .build();
    }
}
