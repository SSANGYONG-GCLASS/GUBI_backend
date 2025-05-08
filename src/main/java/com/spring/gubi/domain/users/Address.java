package com.spring.gubi.domain.users;

import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;
import lombok.*;

@Embeddable
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class Address {
    
    // 우편번호
    @Column(name = "zipcode", nullable = false)
    private String zipcode;
    
    // 주소
    @Column(name = "address", nullable = false)
    private String address;
    
    // 상세 주소
    @Column(name = "detail_address", nullable = false)
    private String detailAddress;

}//and of class...
