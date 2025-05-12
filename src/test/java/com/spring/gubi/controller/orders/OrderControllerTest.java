package com.spring.gubi.controller.orders;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.spring.gubi.config.error.ErrorCode;
import com.spring.gubi.domain.carts.Cart;
import com.spring.gubi.domain.orders.OrderStatus;
import com.spring.gubi.domain.product.Option;
import com.spring.gubi.domain.users.*;
import com.spring.gubi.dto.orders.AddOrderRequest;
import com.spring.gubi.repository.carts.CartRepository;
import com.spring.gubi.repository.products.OptionRepository;
import com.spring.gubi.repository.users.UserRepository;
import com.spring.gubi.repository.users.DeliveryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
public class OrderControllerTest {

    private static final Logger log = LoggerFactory.getLogger(OrderControllerTest.class);
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OptionRepository optionRepository;

    @Autowired
    private DeliveryRepository deliveryRepository;

    private User user;
    private Delivery delivery;
    private Cart cart1;
    private Cart cart2;
    private Cart cart3; // 수량 5000000개
    private Option option1;
    private Option option2;
    private Long notExistId = -1L; // 존재하지 않는 일련번호

    @BeforeEach
    void setUp() {
        Address address = new Address("1234", "Street 2", "City 1");

        User user = User.builder()
                .userid("testOrder")
                .password("qwer1234$")
                .name("테스트")
                .email("testOrder@test.com")
                .role(UserRole.USER)
                .status(UserStatus.ACTIVE)
                .point(100)
                .address(address)
                .build();

        this.user = userRepository.save(user);

        this.delivery = Delivery.builder()
                .user(user)
                .address(address)
                .deliveryName("테스트 배송지")
                .receiver("테스트 수령인")
                .receiverTel("01012341234")
                .isDefault(DeliveryDefault.DEFAULT)
                .build();

        this.delivery = deliveryRepository.save(delivery);

        this.option1 = optionRepository.findById(1L).orElseThrow();

        this.option2 = optionRepository.findById(2L).orElseThrow();

        Cart testCart = Cart.builder()
                .user(user)
                .option(option1)
                .cnt(1)
                .build();

        this.cart1 = cartRepository.save(testCart);

        testCart = Cart.builder()
                .user(user)
                .option(option2)
                .cnt(2)
                .build();

        this.cart2 = cartRepository.save(testCart);

        testCart = Cart.builder()
                .user(user)
                .option(option2)
                .cnt(5000000)
                .build();

        this.cart3 = cartRepository.save(testCart);
    }

    @DisplayName("주문 등록 성공 201 반환")
    @Test
    void 주문_등록_성공() throws Exception {
        List<Long> cartNoList = new ArrayList<>();
        cartNoList.add(cart1.getId());
        cartNoList.add(cart2.getId());

        AddOrderRequest request = AddOrderRequest.builder().userNo(user.getId())
                .deliveryNo(delivery.getId())
                .usePoint(10)
                .status(OrderStatus.ORDER_COMPLETED)
                .cartNoList(cartNoList)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(request);

        log.info("주문 등록 성공 테스트 시작");

        mockMvc.perform(post("/api/orders")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.order.status").value(OrderStatus.ORDER_COMPLETED.toString()));
    }

    @DisplayName("주문 등록 실패 존재하지 않는 회원 404 반환")
    @Test
    void 주문_등록_실패_존재하지_않는_회원() throws Exception {
        List<Long> cartNoList = new ArrayList<>();
        cartNoList.add(cart1.getId());
        cartNoList.add(cart2.getId());

        AddOrderRequest request = AddOrderRequest.builder().userNo(0L)
                .deliveryNo(delivery.getId())
                .usePoint(10)
                .status(OrderStatus.ORDER_COMPLETED)
                .cartNoList(cartNoList)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(request);

        log.info("주문 등록 실패 존재하지 않는 회원 테스트 시작");

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(ErrorCode.USER_NOT_FOUND.getMessage()));
    }

    @DisplayName("주문 등록 실패 존재하지 않는 장바구니 404 반환")
    @Test
    void 주문_등록_실패_존재하지_않는_장바구니() throws Exception {
        List<Long> cartNoList = new ArrayList<>();
        cartNoList.add(notExistId);
        cartNoList.add(cart2.getId());

        AddOrderRequest request = AddOrderRequest.builder().userNo(user.getId())
                .deliveryNo(delivery.getId())
                .usePoint(10)
                .status(OrderStatus.ORDER_COMPLETED)
                .cartNoList(cartNoList)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(request);

        log.info("주문 등록 실패 존재하지 않는 장바구니 테스트 시작");

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(ErrorCode.CART_NOT_FOUND.getMessage()));
    }

    @DisplayName("주문 등록 실패 포인트 잔액 부족 400 반환")
    @Test
    void 주문_등록_실패_포인트_잔액_부족() throws Exception {
        List<Long> cartNoList = new ArrayList<>();
        cartNoList.add(cart1.getId());
        cartNoList.add(cart2.getId());

        AddOrderRequest request = AddOrderRequest.builder().userNo(user.getId())
                .deliveryNo(delivery.getId())
                .usePoint(200)
                .status(OrderStatus.ORDER_COMPLETED)
                .cartNoList(cartNoList)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(request);

        log.info("주문 등록 실패 포인트 잔액 부족 테스트 시작");

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorCode.INSUFFICIENT_POINT_BALANCE.getMessage()));
    }

    @DisplayName("주문 등록 실패 존재하지 않는 배송지 400 반환")
    @Test
    void 주문_등록_실패_존재하지_않는_배송지() throws Exception {
        List<Long> cartNoList = new ArrayList<>();
        cartNoList.add(cart1.getId());
        cartNoList.add(cart2.getId());

        AddOrderRequest request = AddOrderRequest.builder().userNo(user.getId())
                .deliveryNo(notExistId)
                .usePoint(10)
                .status(OrderStatus.ORDER_COMPLETED)
                .cartNoList(cartNoList)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(request);

        log.info("주문 등록 실패 존재하지 않는 배송지 테스트 시작");

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value(ErrorCode.DELIVERY_NOT_FOUND.getMessage()));
    }

    @DisplayName("주문 등록 실패 상품 재고 부족 400 반환")
    @Test
    void 주문_등록_실패_상품_재고_부족() throws Exception {
        List<Long> cartNoList = new ArrayList<>();
        cartNoList.add(cart1.getId());
        cartNoList.add(cart2.getId());
        cartNoList.add(cart3.getId());

        AddOrderRequest request = AddOrderRequest.builder().userNo(user.getId())
                .deliveryNo(delivery.getId())
                .usePoint(10)
                .status(OrderStatus.ORDER_COMPLETED)
                .cartNoList(cartNoList)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(request);

        log.info("주문 등록 실패 상품 재고 부족 테스트 시작");

        mockMvc.perform(post("/api/orders")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value(ErrorCode.OUT_OF_STOCK.getMessage()));
    }

    @DisplayName("주문 조회 성공 200 반환")
    @Test
    void 주문_조회_성공() throws Exception {

        log.info("주문 조회 성공 테스트 시작");

        mockMvc.perform(get("/api/orders?userNo=" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.orders").isArray());

    }
}
