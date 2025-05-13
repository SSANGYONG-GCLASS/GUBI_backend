package com.spring.gubi.service.carts;

import com.spring.gubi.config.error.exception.*;
import com.spring.gubi.domain.carts.Cart;
import com.spring.gubi.domain.orders.Order;
import com.spring.gubi.domain.product.Option;
import com.spring.gubi.domain.users.Delivery;
import com.spring.gubi.domain.users.User;
import com.spring.gubi.dto.orders.*;
import com.spring.gubi.repository.carts.CartRepository;
import com.spring.gubi.repository.carts.OrderRepository;
import com.spring.gubi.repository.users.DeliveryRepository;
import com.spring.gubi.repository.users.UserRepository;
import com.spring.gubi.util.Pagination;
import com.spring.gubi.util.PagingUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final CartRepository cartRepository;
    private final DeliveryRepository deliveryRepository;

    @Transactional(readOnly = true)
    public GetOrderResponse getOrdersByUser_Id(GetOrderRequest request) {
        User user = userRepository.findById(request.getUserNo()).orElseThrow(UserNotFondException::new);

        Page<Order> orders = orderRepository.findByUser_IdAndStatusIn(user.getId(), request.getStatuses(), request.getPageable())
                .orElseThrow();
        Pagination pagination = PagingUtil.getPagination(orders, 5);
        return new GetOrderResponse(orders, pagination);
    }

    @Transactional
    public AddOrderResponse saveOrder(AddOrderRequest request) {
        User user = userRepository.findById(request.getUserNo())
                .orElseThrow(UserNotFondException::new);

        Delivery delivery = deliveryRepository.findByIdAndUser_Id(request.getDeliveryNo(), request.getUserNo())
                .orElseThrow(DeliveryNotFoundException::new);

        List<Cart> carts = cartRepository.findByIdInAndUser_Id(request.getCartNoList(), request.getUserNo());
        // 장바구니가 모두 존재하는지 확인
        if (carts.size() != request.getCartNoList().size()) {
            throw new CartNotFoundException();
        }

        // 상품 재고 차감
        carts.forEach(cart -> {
            Option option = cart.getOption();
            option.decreaseCnt(cart.getCnt());
        });

        // 사용자 포인트 차감
        user.usePoint(request.getUsePoint());

        Order order = orderRepository.save(request.toEntity(user, delivery, carts));

        // 주문 완료 후 장바구니에서 삭제
        cartRepository.deleteAll(carts);

        return new AddOrderResponse(order);
    }

}
