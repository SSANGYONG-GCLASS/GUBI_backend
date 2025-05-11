package com.spring.gubi.domain.orders;

import com.spring.gubi.domain.product.Option;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "order_detail")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
public class OrderDetail {
    @Id
    @Column(name = "order_detailno", nullable = false, unique = true, updatable = false)
    @SequenceGenerator(name = "SEQ_ORDER_DETAIL_GENERATOR", sequenceName = "orderdetail_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ORDER_DETAIL_GENERATOR")
    private Long id;

    @JoinColumn(name = "fk_orderno", referencedColumnName = "orderno", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Order order; // 주문 번호 (FK)

    @JoinColumn(name = "fk_optionno", referencedColumnName = "optionno", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Option option; // 옵션 번호 (FK)

    @Column(name = "cnt", nullable = false)
    private Integer cnt; // 수량

    @Column(name = "price", nullable = false)
    private Integer price; // 단가
}
