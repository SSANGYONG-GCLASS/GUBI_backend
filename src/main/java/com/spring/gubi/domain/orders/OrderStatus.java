package com.spring.gubi.domain.orders;

public enum OrderStatus {
    // 결제대기, 주문완료, 주문취소, 배송중, 배송완료, 구매확정, 환불접수, 환불완료
    PAYMENT_PENDING, ORDER_COMPLETED, ORDER_CANCELLED, SHIPPING, DELIVERED, PURCHASE_CONFIRMED, REFUND_REQUESTED, REFUND_COMPLETED
}
