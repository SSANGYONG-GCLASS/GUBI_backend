package com.spring.gubi.dto.carts;

import com.spring.gubi.domain.carts.Cart;
import com.spring.gubi.util.Pagination;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
public class GetCartResponse {

    private List<GetCartDTO> carts;
    Pagination pagination;

    public GetCartResponse(Page<Cart> carts, Pagination pagination) {
        this.carts = carts.getContent().stream().map(GetCartDTO::new).toList();
        this.pagination = pagination;
    }
}
