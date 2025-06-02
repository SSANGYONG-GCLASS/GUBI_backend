package com.spring.gubi.service.carts;

import com.spring.gubi.config.error.ErrorCode;
import com.spring.gubi.config.error.exception.BusinessBaseException;
import com.spring.gubi.config.error.exception.CartNotFoundException;
import com.spring.gubi.config.error.exception.OptionNotFoundException;
import com.spring.gubi.config.error.exception.UserNotFondException;
import com.spring.gubi.domain.carts.Cart;
import com.spring.gubi.domain.product.Option;
import com.spring.gubi.domain.users.User;
import com.spring.gubi.dto.carts.*;
import com.spring.gubi.repository.carts.CartRepository;
import com.spring.gubi.repository.products.OptionRepository;
import com.spring.gubi.repository.users.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final OptionRepository optionRepository;

    // 한 회원의 장바구니 목록 가져오기
    public GetCartResponse getCartsByUser_Id(String userId) {
        List<Cart> carts = cartRepository.findByUser_Userid(userId);
        return new GetCartResponse(carts);
    }

    // 장바구니 저장, 같은 회원의 같은 상품 옵션이 존재하면 수량을 추가
    @Transactional
    public void saveCart(String userId, AddCartRequest request) {
        User user = userRepository.findByUserid(userId)
                .orElseThrow(UserNotFondException::new);

        Option option = optionRepository.findById(request.getOptionNo())
                .orElseThrow(OptionNotFoundException::new);

        // 상품의 재고가 부족하면 장바구니에 추가하지 않음
        if(option.getCnt() < request.getCnt()) {
            throw new BusinessBaseException(ErrorCode.OUT_OF_STOCK);
        }

        cartRepository.findByUser_IdAndOption_Id(user.getId(), option.getId())
                .map(cart -> {
                    cart.updateCnt(UpdateCartCntRequest.builder()
                            .cnt(cart.getCnt() + request.getCnt())
                            .build());
                    return cart;
                })
                .orElseGet(() -> cartRepository.save(request.toEntity(user, option)));
    }

    // 장바구니 수량을 변경
    @Transactional
    public void updateCart(String userId, Long id, UpdateCartCntRequest request) {
        Cart cart = cartRepository.findByIdAndUser_Userid(id, userId)
                .orElseThrow(CartNotFoundException::new);

        if(cart.getOption().getCnt() > request.getCnt()) { // 상품 재고가 장바구니 수량보다 많은 경우
            cart.updateCnt(request);
        }
        else { // 상품 재고보다 장바구니 수량이 많은 경우
            request.setCnt(cart.getOption().getCnt()); // 재고 최대 수량으로 변경
            cart.updateCnt(request);
        }
    }

    // 장바구니 삭제
    @Transactional
    public void deleteCart(String userId, Long id) {
        Cart cart = cartRepository.findByIdAndUser_Userid(id, userId).orElseThrow(CartNotFoundException::new);
        cartRepository.delete(cart);
    }

    // 장바구니 일련번호 리스트로 장바구니 목록 가져오기(결제 시 필요)
    public GetCartForOrderResponse getCartsByIdIn(String userId, GetCartForOrderRequest request) {
        List<Cart> carts = cartRepository.findByIdInAndUser_Userid(request.getCartNoList(), userId);
        return new GetCartForOrderResponse(carts);
    }
}
