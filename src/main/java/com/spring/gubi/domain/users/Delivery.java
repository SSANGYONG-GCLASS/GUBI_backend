package com.spring.gubi.domain.users;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name="delivery") // 데이터베이스 테이블 이름
@Getter
@Builder
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
public class Delivery {
    
    // 배송지 시퀀스
    @Id
    @Column(name = "deliveryno", nullable = false, unique = true, updatable = false) // 데이터베이스 컬럼명, null 허용안됌,
    @SequenceGenerator(name = "SEQ_DELIVERY_GENERATOR", sequenceName = "delivery_seq", allocationSize = 1) // 진짜 시퀀스명, 사이즈크기
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_DELIVERY_GENERATOR") // 이게 진짜 시퀀스 만들어줌
    private Long id; //프라이머리키
    
    //
    @JoinColumn(name = "fk_user_no", nullable = false) // not null
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL) // 케스케이트
    private User user;
    
    // 배송지명
    @Column(name = "delivery_name", nullable = false)
    private String deliveryName;
    
    // 수령인
    @Column(name = "receiver", nullable = false)
    private String receiver;
    
    // 수령인 전화번호
    @Column(name = "receiver_tel", nullable = false)
    private String receiverTel;
    
    // 주소클래스의 컬럼을 모두 가져옴(우편번호, 주소, 상세주소)
    @Embedded
    private Address address;
    
    // 배송 시 요청사항
    @Column(name = "memo")
    private String memo;
    
    // 기본배송지 여부
    @Column(name = "is_default", nullable = false)
    @Enumerated(EnumType.STRING)
    private DeliveryDefault isDefault; // 기본배송지 여부 (ENUM: DEFAULT, NONE)
    
    
    // fk_user_no BIGINT NOT NULL,                                     -- 외래키: users.user_no
    // delivery_name VARCHAR(255) NOT NULL,                            -- 배송지명
    // receiver VARCHAR(255) NOT NULL,                                 -- 수령인
    // receiver_tel VARCHAR(255) NOT NULL,                             -- 수령인 연락처
    // -- [주소] : Address 클래스의 @Embeddable 구성
    // zipcode VARCHAR(20),                                            -- 우편번호
    // address VARCHAR(255),                                           -- 주소
    // detail_address VARCHAR(255),                                    -- 상세주소
    //
    // memo VARCHAR(255),                                              -- 배송 요청사항
    // is_default VARCHAR(255)                                          -- 기본배송지 여부 (ENUM: DEFAULT, NONE)


}//end of class...
