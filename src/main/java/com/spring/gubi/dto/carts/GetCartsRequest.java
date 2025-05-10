package com.spring.gubi.dto.carts;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
@Setter
public class GetCartsRequest {
    private Long userNo; // 회원 일련번호, TODO: 나중에 토큰 또는 세션에서 가져오게 되면 삭제할 것
    private int page = 1;
    private int size = 10;

    public Pageable getPageable() {
        return PageRequest.of(page-1, size, Sort.Direction.DESC, "id");
    }
}
