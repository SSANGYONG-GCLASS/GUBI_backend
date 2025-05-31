package com.spring.gubi.dto.users;

import com.spring.gubi.domain.users.Delivery;
import com.spring.gubi.util.Pagination;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
public class GetDeliverysResponse {
    
    private List<GetDeliveryDTO> deliveries;
    
    // 페이지네이션 객체
    private Pagination pagination; //
    
    
    // 딜리버리 엔티티타입으로 나온 리스트를 GetDeliveryDTO 타입의 리스트로 만들어주는 메소드
    public GetDeliverysResponse(Page<Delivery> deliveryList, Pagination pagination) {
        // stteam 은 for 문이랑 똑같고 map 은 () 안의 조건문이다.
        // 생성자 객체로 선언! (페이지 객체에서 리스트를 가져오려면 getContent() 사용! 그래야 stream() 사용가능)
        this.deliveries = deliveryList.getContent().stream().map(GetDeliveryDTO::new).collect(Collectors.toList());
        this.pagination = pagination;
    }



}//end of class...
