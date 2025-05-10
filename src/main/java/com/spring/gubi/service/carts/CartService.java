package com.spring.gubi.service.carts;

import com.spring.gubi.config.error.exception.CartNotFoundException;
import com.spring.gubi.config.error.exception.OptionNotFoundException;
import com.spring.gubi.config.error.exception.UserNotFondException;
import com.spring.gubi.domain.carts.Cart;
import com.spring.gubi.domain.product.Option;
import com.spring.gubi.domain.users.User;
import com.spring.gubi.dto.carts.AddCartRequest;
import com.spring.gubi.dto.carts.GetCartsRequest;
import com.spring.gubi.dto.carts.GetCartsResponse;
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

    @Transactional(readOnly = true)
    public GetCartsResponse getCartsByUser_Id(GetCartsRequest request) {
        User user = userRepository.findById(request.getUserNo()).orElseThrow(UserNotFondException::new);

        Page<Cart> carts = cartRepository.findByUser_Id(user.getId(), request.getPageable());

        Pagination pagination = PagingUtil.getPagination(carts, 5);
        return new GetCartsResponse(carts, pagination);
    }

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

    @Transactional
    public void updateCart(Long id, UpdateCartCntRequest request) {
        Cart cart = cartRepository.findById(id).orElseThrow(CartNotFoundException::new);
        cart.updateCnt(request);
    }

    @Transactional
    public void deleteCart(Long id) {
        Cart cart = cartRepository.findById(id).orElseThrow(CartNotFoundException::new);
        cartRepository.delete(cart);
    }
}
