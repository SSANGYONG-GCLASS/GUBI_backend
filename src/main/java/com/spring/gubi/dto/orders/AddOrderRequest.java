package com.spring.gubi.dto.orders;

import com.spring.gubi.domain.carts.Cart;
import com.spring.gubi.domain.orders.Order;
import com.spring.gubi.domain.orders.OrderDetail;
import com.spring.gubi.domain.orders.OrderStatus;
import com.spring.gubi.domain.users.Delivery;
import com.spring.gubi.domain.users.User;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
@Setter
@Builder
public class AddOrderRequest {
    private Long userNo; // 회원 번호 (FK)
    private Long deliveryNo; // 배송지 번호 (FK)
    private Integer usePoint; // 포인트 사용금액
    private OrderStatus status; // 주문상태 (ENUM: 결제대기... 환불완료)

    private List<Long> cartNoList; // 장바구니 일련번호 리스트 (FK)

    public Order toEntity(User user, Delivery delivery, List<Cart> carts) {

        // 총 주문 금액
        long totalPrice = carts.stream()
                .mapToLong(cart -> (long) cart.getOption().getProduct().getPrice() * cart.getCnt())
                .sum();

        // 배송비
        // 총 주문금액이 30만원 이상이면 무료, 아니면 가장 저렴한 배송비 부담
        int deliveryPrice = (totalPrice > 300000) ? 0 : carts.stream()
                .mapToInt(cart -> cart.getOption().getProduct().getDelivery_price())
                .min()
                .orElseThrow();

        totalPrice += deliveryPrice;

        // 포인트 적립금
        int rewardPoint = carts.stream()
                .mapToInt(cart -> cart.getOption().getProduct().getPoint_pct() * cart.getOption().getProduct().getPrice())
                .sum();

        // 주문
        Order order = Order.builder()
                .user(user)
                .delivery(delivery)
                .totalPrice(totalPrice)
                .usePoint(usePoint)
                .rewardPoint(rewardPoint)
                .deliveryPrice(deliveryPrice)
                .totalCnt(carts.stream().mapToInt(Cart::getCnt).sum())
                .status(status)
                .build();

        // 주문 상세
        List<OrderDetail> orderDetails = carts
                .stream()
                .map(cart -> {
                    Integer price = cart.getOption().getProduct().getPrice() * cart.getCnt();
                    return OrderDetail.builder()
                            .order(order)
                            .option(cart.getOption())
                            .cnt(cart.getCnt())
                            .price(price)
                            .build();
                })
                .collect(Collectors.toList());

        order.updateOrderDetails(orderDetails);

        return order;
    }
}
