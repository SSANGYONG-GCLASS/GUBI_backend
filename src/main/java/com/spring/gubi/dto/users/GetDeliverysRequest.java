package com.spring.gubi.dto.users;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

@Getter
@Setter
public class GetDeliverysRequest {
    
    private Long userNo; // 회원 일련번호, TODO: 나중에 토큰 또는 세션에서 가져오게 되면 삭제할 것
    private int page = 1; // 페이지 첫 번호
    private int size = 10; // 임의로 만들어준 페이지 양
    
    // 페이지블 형태로 만들어주는 메소드
    public Pageable getPageable() {
        return PageRequest.of(page-1, size, Sort.Direction.DESC, "id");
    }// 



}//end of class...
