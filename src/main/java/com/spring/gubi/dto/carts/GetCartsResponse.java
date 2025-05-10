package com.spring.gubi.dto.carts;

import com.spring.gubi.domain.carts.Cart;
import com.spring.gubi.util.Pagination;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
public class GetCartsResponse {

    private List<GetCartResponse> carts;
    Pagination pagination;

    public GetCartsResponse(Page<Cart> carts, Pagination pagination) {
        this.carts = carts.getContent().stream().map(GetCartResponse::new).toList();
        this.pagination = pagination;
    }
}
