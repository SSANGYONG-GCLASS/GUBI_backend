package com.spring.gubi.dto.orders;

import com.spring.gubi.domain.users.User;
import lombok.Getter;

@Getter
public class GetUserDetailForOrderResponse {
    String name;
    String email;
    String tel;
    int point;

    public GetUserDetailForOrderResponse(User user) {
        this.name = user.getName();
        this.email = user.getEmail();
        this.tel = user.getTel();
        this.point = user.getPoint();
    }
}
