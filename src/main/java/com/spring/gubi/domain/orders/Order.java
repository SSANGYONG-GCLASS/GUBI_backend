package com.spring.gubi.domain.orders;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "orders")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
public class Order {

    @Id
    @Column(name = "orderno", nullable = false, unique = true, updatable = false)
    @SequenceGenerator(name = "SEQ_ORDER_GENERATOR", sequenceName = "order_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_ORDER_GENERATOR")
    private Long id; // 주문 번호 (시퀀스 자동 증가)

    @JoinColumn(name = "fk_user_no", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private User user; // 회원 번호 (FK)

    @JoinColumn(name = "fk_deliveryno", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private Delivery delivery; // 배송지 번호 (FK)

    @Column(name = "total_price", nullable = false)
    private Long total_price; // 총 주문 금액

    @Column(name = "use_point", nullable = false)
    private Integer use_point; // 포인트 사용금액

    @Column(name = "reward_point", nullable = false)
    private Integer reward_point; // 포인트 적립금

    @Column(name = "delivery_price", nullable = false)
    private Integer delivery_price; // 배송비

    @Column(name = "total_cnt", nullable = false)
    private Integer total_cnt; // 총 수량

    @Column(name = "orderday")
    @Builder.Default
    private LocalDateTime orderday = LocalDateTime.now(); // 주문 일자

    @Column(name = "status")
    @Enumerated(EnumType.STRING)
    private String status; // 주문상태 (ENUM: 결제대기... 환불완료)
    // PAYMENT_PENDING, ORDER_COMPLETED, ORDER_CANCELLED, SHIPPING, DELIVERED, PURCHASE_CONFIRMED, REFUND_REQUESTED, REFUND_COMPLETED

    @Column(name = "delivery_date")
    private LocalDateTime delivery_date; // 배송완료일자

}
