package com.spring.gubi.controller.reviews;

import com.spring.gubi.domain.reviews.Review;
import com.spring.gubi.domain.users.User;
import com.spring.gubi.domain.users.UserRole;
import com.spring.gubi.domain.users.UserStatus;
import com.spring.gubi.repository.reviews.ReviewRepository;
import com.spring.gubi.repository.users.UserRepository;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@ActiveProfiles("test")
class ReviewControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ReviewRepository reviewRepository;

    // 테스트 시작 전 유저 정보 세팅
//    @BeforeEach
//    void setUp() {
//        User user = User.builder()
//                .userid("test")
//                .password("qwer1234$")
//                .name("테스트")
//                .email("test@test.com")
//                .role(UserRole.USER)
//                .status(UserStatus.ACTIVE)
//                .build();
//
//        userRepository.save(user);
//    }


    @DisplayName("이미지 없는 리뷰작성 성공 201 반환")
    @Test
    void 이미지없는_리뷰작성_성공 () throws Exception {
        // given: 테스트 데이터 준비

        String title = "제품 후기입니다..";

        // Form 전송 데이터
        MockMultipartFile data = new MockMultipartFile(
                "data", "", MediaType.APPLICATION_JSON_VALUE,
        """
            {
                "userNo": 1,
                "optionNo": 1,
                "title": "%s",
                "content": "너무 좋네요",
                "score": 5
            }
        """.formatted(title).getBytes());


        // when + then: 컨트롤러 테스트(응답 받은 title 이 동일한지 검증, 등록한 이미지가 없어야 함)
        mockMvc.perform(multipart("/api/reviews")
                .file(data)
                .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(title))
                .andExpect(jsonPath("$.img").isEmpty());
    }// end of void 이미지없는_리뷰작성_성공 () throws Exception ------------------


    @DisplayName("이미지 있는 리뷰작성 성공 201 반환")
    @Test
    void 이미지있는_리뷰작성_성공 () throws Exception {
        // given: 테스트 데이터 준비

        String title = "제품 후기입니다..";

        // 이미지
        MockMultipartFile img = new MockMultipartFile(
                "img", "test.png", MediaType.IMAGE_PNG_VALUE, "img".getBytes()
        );
//        MockMultipartFile img = new MockMultipartFile(
//                "img", "test.jpg", MediaType.IMAGE_JPEG_VALUE, "img".getBytes()
//        );

        // Form 전송 데이터
        MockMultipartFile data = new MockMultipartFile(
        "data", "", MediaType.APPLICATION_JSON_VALUE,
        """
            {
                "userNo": 1,
                "optionNo": 1,
                "title": "%s",
                "content": "너무 좋네요",
                "score": 5
            }
        """.formatted(title).getBytes());


        // when + then: 컨트롤러 테스트(응답 받은 리뷰제목이 동일한지 검증, 이미지가 포함되어야 함)
        mockMvc.perform(multipart("/api/reviews")
                        .file(img)
                        .file(data)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                        .andExpect(status().isCreated())
                        .andExpect(jsonPath("$.title").value(title))
                        .andExpect(jsonPath("$.img").isNotEmpty());
    }// end of void 이미지있는_리뷰작성_성공 () throws Exception ------------------


    @DisplayName("존재하지 않는 유저 리뷰작성 요청 404 에러 반환")
    @Test
    void 존재하지_않는_유저로_리뷰작성시_실패 () throws Exception {
        // given: 테스트 데이터 준비
        // Form 전송 데이터
        MockMultipartFile data = new MockMultipartFile(
        "data", "", MediaType.APPLICATION_JSON_VALUE,
        """
            {
                "userNo": 9999,
                "optionNo": 1,
                "title": "히히",
                "content": "너무 좋네요",
                "score": 5
            }
        """.getBytes());

        // when + then: 404 에러 반환
        mockMvc.perform(multipart("/api/reviews")
                    .file(data)
                    .contentType(MediaType.MULTIPART_FORM_DATA))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value("존재하지 않는 회원입니다."));
    }// end of void 존재하지_않는_유저로_리뷰작성시_실패 () throws Exception --------------------


    @DisplayName("존재하지 않는 상품에 리뷰작성 요청 404 에러 반환")
    @Test
    void 존재하지_않는_상품에_리뷰작성시_실패 () throws Exception {
        // given: 테스트 데이터 준비
        // Form 전송 데이터
        MockMultipartFile data = new MockMultipartFile(
        "data", "", MediaType.APPLICATION_JSON_VALUE,
        """
            {
                "userNo": 1,
                "optionNo": 9999,
                "title": "히히",
                "content": "너무 좋네요",
                "score": 5
            }
        """.getBytes());

        // when + then: 404 에러 반환
        mockMvc.perform(multipart("/api/reviews")
                        .file(data)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                        .andExpect(status().isNotFound())
                        .andExpect(jsonPath("$.message").value("존재하지 않는 상품 옵션입니다."));
    }// end of void 존재하지_않는_유저로_리뷰작성시_실패 () throws Exception --------------------


    @DisplayName("확장자가 없는 이미지파일 리뷰작성 성공 201 반환")
    @Test
    void 확장자없는_이미지_리뷰작성_성공 () throws Exception {
        // given: 테스트 데이터 준비

        String title = "제품 후기입니다..";

        // 이미지
//        MockMultipartFile img = new MockMultipartFile(
//                "img", "test.png", MediaType.IMAGE_PNG_VALUE, "img".getBytes()
//        );
        MockMultipartFile img = new MockMultipartFile(
                "img", "test", MediaType.IMAGE_JPEG_VALUE, "img".getBytes()
        );

        // Form 전송 데이터
        MockMultipartFile data = new MockMultipartFile(
                "data", "", MediaType.APPLICATION_JSON_VALUE,
                """
                    {
                        "userNo": 1,
                        "optionNo": 1,
                        "title": "%s",
                        "content": "너무 좋네요",
                        "score": 5
                    }
                """.formatted(title).getBytes());


        // when + then: 컨트롤러 테스트(응답 받은 리뷰제목이 동일한지 검증, 이미지가 포함되어야 함)
        mockMvc.perform(multipart("/api/reviews")
                        .file(img)
                        .file(data)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value(title))
                .andExpect(jsonPath("$.img").value(Matchers.endsWith(".png")));
    }// end of void 이미지있는_리뷰작성_성공 () throws Exception ------------------


    @DisplayName("이미지 없는 리뷰 수정")
    @Test
    void 이미지_없는_리뷰_수정_테스트() throws Exception {
        // given: 테스트 데이터 준비

        String title = "제품 후기입니다..";

        // Form 전송 데이터
        MockMultipartFile data = new MockMultipartFile(
                "data", "", MediaType.APPLICATION_JSON_VALUE,
                """
                    {
                        "id": 38,
                        "title": "%s",
                        "content": "너무 좋네요",
                        "score": 5
                    }
                """.formatted(title).getBytes());


        // when + then: 컨트롤러 테스트(응답 받은 title 이 동일한지 검증, 등록한 이미지가 없어야 함)
        mockMvc.perform(multipart(HttpMethod.PUT, "/api/reviews")
                        .file(data)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(title))
                .andExpect(jsonPath("$.img").isEmpty());
    }// end of void 이미지_없는_리뷰_수정_테스트() throws Exception --------------


    @DisplayName("이미지 포함 리뷰 수정")
    @Test
    void 기존이미지가_있는_리뷰_수정_테스트() throws Exception {
        // given: 테스트 데이터 준비
        Review review = reviewRepository.findById(37L).orElseThrow();

        String oldImg = review.getImg();

        String title = "제품 후기입니다..";

        // 이미지
        MockMultipartFile img = new MockMultipartFile(
                "img", "test.png", MediaType.IMAGE_PNG_VALUE, "img".getBytes()
        );

        // Form 전송 데이터
        MockMultipartFile data = new MockMultipartFile(
                "data", "", MediaType.APPLICATION_JSON_VALUE,
                """
                    {
                        "id": 37,
                        "title": "%s",
                        "content": "너무 좋네요",
                        "score": 5
                    }
                """.formatted(title).getBytes());


        // when + then: 컨트롤러 테스트(응답 받은 title 이 동일한지 검증, 등록한 이미지가 없어야 함)
        mockMvc.perform(multipart(HttpMethod.PUT, "/api/reviews")
                        .file(img)
                        .file(data)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value(title))
                .andExpect(jsonPath("$.img").value(Matchers.matchesPattern("^[a-z0-9]{32}\\.png$")))
                .andExpect(jsonPath("$.img").value(Matchers.not(oldImg)));
    }// end of void 이미지_포함_리뷰_수정_테스트() throws Exception --------------


}