package com.spring.gubi.service.reviews;

import com.spring.gubi.config.error.exception.OptionNotFoundException;
import com.spring.gubi.config.error.exception.UserNotFondException;
import com.spring.gubi.domain.product.Option;
import com.spring.gubi.domain.reviews.Review;
import com.spring.gubi.domain.users.User;
import com.spring.gubi.dto.reviews.WriteReviewRequest;
import com.spring.gubi.dto.reviews.WriteReviewResponse;
import com.spring.gubi.repository.products.OptionRepository;
import com.spring.gubi.repository.reviews.ReviewRepository;
import com.spring.gubi.repository.users.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.willDoNothing;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @InjectMocks
    private ReviewService reviewService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private OptionRepository optionRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @Mock
    private MultipartFile multipartFile;

    @BeforeEach
    void setUp() {
        // uploadPath 필드에 uploads/reviews 값 주입
        ReflectionTestUtils.setField(reviewService, "uploadPath", "uploads/reviews");
    }

    @DisplayName("이미지 포함한 리뷰작성 성공")
    @Test
    void 리뷰작성_성공_이미지포함() throws Exception {
        // given: 유저, 옵션 정보, 리뷰 작성 요청 정보 작성
        User mockUser = User.builder().id(1L).name("테스트").build();
        Option mockOption = Option.builder().id(1L).name("옵션테스트").color("옵션컬러2132").build();

        given(userRepository.findById(1L)).willReturn(Optional.of(mockUser));
        given(optionRepository.findById(1L)).willReturn(Optional.of(mockOption));


        // 리뷰 정보 생성
        WriteReviewRequest request = new WriteReviewRequest();
        request.setUserNo(1L);
        request.setOptionNo(1L);
        request.setTitle("후기입니다...");
        request.setContent("좋습니다..");
        request.setScore(5);


        // 이미지 mock
        given(multipartFile.isEmpty()).willReturn(false);
        given(multipartFile.getOriginalFilename()).willReturn("test.png");
        request.setImg("test.png");

        // 실제 파일 저장 방지
        willDoNothing().given(multipartFile).transferTo(any(File.class));

        // Review mock 객체 생성
        Review mockReview = request.toEntity(mockUser, mockOption);

        given(reviewRepository.save(any(Review.class))).willReturn(mockReview);

        // when: 저장 실행
        WriteReviewResponse response = reviewService.save(request, multipartFile);

        // then: 결과 비교
        assertThat(response.getTitle()).isEqualTo(request.getTitle());
        assertThat(response.getUserNo()).isEqualTo(request.getUserNo());
        assertThat(response.getImg()).isNotBlank();

    }// end of void 리뷰작성_성공_이미지포함() throws Exception ---------------------


    @DisplayName("이미지 제외한 리뷰작성 성공")
    @Test
    void 리뷰작성_성공_이미지제외() throws Exception {
        // given: 유저, 옵션 정보, 리뷰 작성 요청 정보 작성
        User mockUser = User.builder().id(1L).name("테스트").build();
        Option mockOption = Option.builder().id(1L).name("옵션테스트").color("옵션컬러2132").build();

        given(userRepository.findById(1L)).willReturn(Optional.of(mockUser));
        given(optionRepository.findById(1L)).willReturn(Optional.of(mockOption));


        // 리뷰 정보 생성
        WriteReviewRequest request = new WriteReviewRequest();
        request.setUserNo(1L);
        request.setOptionNo(1L);
        request.setTitle("후기입니다...");
        request.setContent("좋습니다..");
        request.setScore(5);


        // Review mock 객체 생성
        Review mockReview = request.toEntity(mockUser, mockOption);

        given(reviewRepository.save(any(Review.class))).willReturn(mockReview);

        // when: 저장 실행
        WriteReviewResponse response = reviewService.save(request, null);

        // then: 결과 비교
        assertThat(response.getTitle()).isEqualTo(request.getTitle());
        assertThat(response.getUserNo()).isEqualTo(request.getUserNo());
        assertThat(response.getImg()).isBlank();

    }// end of void 리뷰작성_성공_이미지포함() throws Exception ---------------------


    @DisplayName("존재하지 않는 회원일 경우 예외")
    @Test
    void 존재하지_않는_회원_예외() throws Exception {
        // given
        WriteReviewRequest request = new WriteReviewRequest();
        request.setUserNo(9999L);
        given(userRepository.findById(request.getUserNo())).willReturn(Optional.empty());

        // when + then
        assertThrows(UserNotFondException.class, () -> reviewService.save(request, null));

    }// end of void 존재하지_않는_회원_예외() throws Exception -----------


    @DisplayName("존재하지 않는 옵션 예외")
    @Test
    void 존재하지_않는_옵션_예외() throws Exception {
        // given: 회원은 유효, 옵션에서 탈락
        User mockUser = User.builder().id(1L).name("테스트").build();

        WriteReviewRequest request = new WriteReviewRequest();
        request.setUserNo(1L);
        request.setOptionNo(9999L);

        given(userRepository.findById(request.getUserNo())).willReturn(Optional.of(mockUser));
        given(optionRepository.findById(request.getOptionNo())).willReturn(Optional.empty());

        // when + then
        assertThrows(OptionNotFoundException.class, () -> reviewService.save(request, null));
    }// end of void 존재하지_않는_옵션_예외() throws Exception -------------------------

}