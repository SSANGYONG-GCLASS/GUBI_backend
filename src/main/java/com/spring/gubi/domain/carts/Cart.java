package com.spring.gubi.domain.carts;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "carts")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
public class Cart {

    @Id
    @Column(name = "cartno", nullable = false, unique = true, updatable = false)
    @SequenceGenerator(name = "SEQ_CART_GENERATOR", sequenceName = "cart_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_CART_GENERATOR")
    private Long id; // 장바구니 번호 (시퀀스)

    @JoinColumn(name = "fk_optionno", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Option option; // 옵션 번호 (FK)

    @JoinColumn(name = "fk_user_no", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private User user; // 회원 번호 (FK)

    @Column(name = "cnt", nullable = false)
    private Integer cnt; // 수량

}
