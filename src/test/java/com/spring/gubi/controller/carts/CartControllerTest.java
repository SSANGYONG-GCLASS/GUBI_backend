package com.spring.gubi.controller.carts;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.jayway.jsonpath.JsonPath;
import com.spring.gubi.domain.carts.Cart;
import com.spring.gubi.domain.product.Option;
import com.spring.gubi.domain.users.User;
import com.spring.gubi.dto.carts.AddCartRequest;
import com.spring.gubi.dto.carts.UpdateCartCntRequest;
import com.spring.gubi.dto.users.LoginUserRequest;
import com.spring.gubi.repository.carts.CartRepository;
import com.spring.gubi.repository.products.OptionRepository;
import com.spring.gubi.repository.users.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockCookie;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
@TestPropertySource(properties = {
        "jwt.secret=TestKeyJSDFKJLkjlkjlksjfdlkjflskdjSff"
})
public class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private OptionRepository optionRepository;

    private User user;
    private Cart cart;
    private Option option;
    private MockCookie accessTokenCookie;

    @BeforeEach
    void setUp() throws Exception {

        this.user = userRepository.findById(2L).orElseThrow();

        LoginUserRequest loginUserRequest = new LoginUserRequest();
        loginUserRequest.setUserid(user.getUserid());
        loginUserRequest.setPassword("qwer1234$");

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(loginUserRequest);

        MvcResult result = mockMvc.perform(post("/api/user/login")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("로그인 성공"))
                .andReturn();

        String response = result.getResponse().getContentAsString();
        String accessToken = JsonPath.read(response, "$.accessToken");

        // 쿠키로 저장
        accessTokenCookie = new MockCookie("accessToken", accessToken);
        accessTokenCookie.setHttpOnly(true);

        this.option = optionRepository.findById(1L).orElseThrow();

        Cart testCart = Cart.builder()
                .user(user)
                .option(option)
                .cnt(1)
                .build();

        this.cart = cartRepository.save(testCart);


    }

    @DisplayName("장바구니 등록 성공 201 반환")
    @Test
    void 장바구니_등록_성공() throws Exception {
        AddCartRequest request = AddCartRequest.builder()
                .optionNo(option.getId())
                .cnt(1)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/carts")
                        .cookie(accessTokenCookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.message").value("장바구니에 추가되었습니다."));
    }// end of void 장바구니_등록_성공() throws Exception -------------

    @DisplayName("장바구니 등록 실패 존재하지 않는 상품 옵션 404 반환")
    @Test
    void 장바구니_등록_실패_존재하지_않는_상품_옵션() throws Exception {
        AddCartRequest request = AddCartRequest.builder()
                .optionNo(0L)
                .cnt(1)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(post("/api/carts")
                        .cookie(accessTokenCookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("존재하지 않는 상품 옵션입니다."));
    }// end of void 장바구니_등록_실패_존재하지_않는_상품_옵션() throws Exception ----------

    @DisplayName("장바구니 조회 성공 200 반환")
    @Test
    void 장바구니_조회_성공() throws Exception {
        mockMvc.perform(get("/api/carts")
                        .cookie(accessTokenCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.carts").isArray());
    }// end of void 장바구니_조회_성공() throws Exception ---------------

    @DisplayName("장바구니 수정 성공 200 반환")
    @Test
    void 장바구니_수정_성공() throws Exception {
        UpdateCartCntRequest request = UpdateCartCntRequest.builder()
                .cnt(1)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(put("/api/carts/"+cart.getId())
                        .cookie(accessTokenCookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("장바구니가 수정되었습니다."));
    }// end of void 장바구니_수정_성공() throws Exception --------------

    @DisplayName("장바구니 수정 실패 존재하지 않는 장바구니 404 반환")
    @Test
    void 장바구니_수정_실패_존재하지_않는_장바구니() throws Exception {
        UpdateCartCntRequest request = UpdateCartCntRequest.builder()
                .cnt(1)
                .build();

        ObjectMapper objectMapper = new ObjectMapper();
        String json = objectMapper.writeValueAsString(request);

        mockMvc.perform(put("/api/carts/0")
                        .cookie(accessTokenCookie)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("존재하지 않는 장바구니입니다."));
    }// end of void 장바구니_수정_실패_존재하지_않는_장바구니() throws Exception --------------

    @DisplayName("장바구니 삭제 성공 200 반환")
    @Test
    void 장바구니_삭제_성공() throws Exception {
        mockMvc.perform(delete("/api/carts/"+cart.getId())
                        .cookie(accessTokenCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("장바구니가 삭제되었습니다."));
    }// end of void 장바구니_삭제_성공() throws Exception ---------------

    @DisplayName("장바구니 삭제 실패 존재하지 않는 장바구니 404 반환")
    @Test
    void 장바구니_삭제_실패_존재하지_않는_장바구니() throws Exception {
        mockMvc.perform(delete("/api/carts/0")
                        .cookie(accessTokenCookie))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.message").value("존재하지 않는 장바구니입니다."));
    }// end of void 장바구니_삭제_실패_존재하지_않는_장바구니() throws Exception---------------

    @DisplayName("주문을 위한 장바구니 조회 성공 200 반환")
    @Test
    void 주문을_위한_장바구니_조회_성공() throws Exception {
        mockMvc.perform(get("/api/carts/for-order?cartNoList="+cart.getId())
                        .cookie(accessTokenCookie))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.carts").isArray());
    }// end of void 주문을_위한_장바구니_조회_성공() throws Exception ---------------

}
