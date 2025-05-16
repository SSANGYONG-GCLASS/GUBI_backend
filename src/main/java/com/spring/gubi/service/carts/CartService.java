package com.spring.gubi.service.carts;

import com.spring.gubi.config.error.exception.CartNotFoundException;
import com.spring.gubi.config.error.exception.OptionNotFoundException;
import com.spring.gubi.config.error.exception.UserNotFondException;
import com.spring.gubi.domain.carts.Cart;
import com.spring.gubi.domain.product.Option;
import com.spring.gubi.domain.users.User;
import com.spring.gubi.dto.carts.AddCartRequest;
import com.spring.gubi.dto.carts.GetCartRequest;
import com.spring.gubi.dto.carts.GetCartResponse;
import com.spring.gubi.dto.carts.UpdateCartCntRequest;
import com.spring.gubi.repository.carts.CartRepository;
import com.spring.gubi.repository.products.OptionRepository;
import com.spring.gubi.repository.users.UserRepository;
import com.spring.gubi.util.Pagination;
import com.spring.gubi.util.PagingUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class CartService {
    private final CartRepository cartRepository;
    private final UserRepository userRepository;
    private final OptionRepository optionRepository;

    // 한 회원의 장바구니 목록 가져오기
    @Transactional(readOnly = true)
    public GetCartResponse getCartsByUser_Id(GetCartRequest request) {
        User user = userRepository.findById(request.getUserNo()).orElseThrow(UserNotFondException::new);

        Page<Cart> carts = cartRepository.findByUser_Id(user.getId(), request.getPageable());

        Pagination pagination = PagingUtil.getPagination(carts, 5);
        return new GetCartResponse(carts, pagination);
    }

    // 장바구니 저장, 같은 회원의 같은 상품 옵션이 존재하면 수량을 추가
    @Transactional
    public void saveCart(AddCartRequest request) {
        User user = userRepository.findById(request.getUserNo())
                .orElseThrow(UserNotFondException::new);

        Option option = optionRepository.findById(request.getOptionNo())
                .orElseThrow(OptionNotFoundException::new);

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
    public void updateCart(Long id, UpdateCartCntRequest request) {
        Cart cart = cartRepository.findByIdAndUser_Id(id, request.getUserNo())
                .orElseThrow(CartNotFoundException::new);
        cart.updateCnt(request);
    }

    // 장바구니 삭제
    @Transactional
    public void deleteCart(Long id) {
        Cart cart = cartRepository.findById(id).orElseThrow(CartNotFoundException::new); // TODO: 로그인 유저 확인하기
        cartRepository.delete(cart);
    }
}
