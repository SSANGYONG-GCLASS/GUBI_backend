package com.spring.gubi.controller.reviews;

import com.spring.gubi.domain.reviews.Review;
import com.spring.gubi.dto.reviews.ReviewDetailResponse;
import com.spring.gubi.dto.reviews.UpdateReviewRequest;
import com.spring.gubi.dto.reviews.WriteReviewRequest;
import com.spring.gubi.dto.reviews.WriteReviewResponse;
import com.spring.gubi.service.reviews.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@RestController
@Slf4j
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
    }


    // 리뷰 작성
    @PostMapping(value = "/api/reviews", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<WriteReviewResponse> writeReview(@RequestPart("data") WriteReviewRequest request,
                                         @RequestPart(value = "img", required = false) MultipartFile img) throws IOException {

        WriteReviewResponse response = reviewService.save(request, img);
        log.info("등록한 리뷰 정보: {}", response);

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }// end of public ResponseEntity<WriteReviewResponse> writeReview(...) ----------------


    // 리뷰 수정
    @PutMapping(value = "/api/reviews", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<WriteReviewResponse> updateReview(@RequestPart("data") UpdateReviewRequest request,
                                         @RequestPart(value = "img", required = false) MultipartFile img) throws IOException {

        WriteReviewResponse response = reviewService.update(request, img);
        log.info("수정한 리뷰 정보: {}", response);

        return ResponseEntity.status(HttpStatus.OK).body(response);
    }// end of public ResponseEntity<WriteReviewResponse> updateReview(...) ----------------


    // 리뷰 삭제
    @DeleteMapping("/api/reviews/{id}")
    public ResponseEntity<Map<String, String>> deleteReview(@PathVariable("id") Long id) {
        reviewService.delete(id);
        return ResponseEntity.status(HttpStatus.OK).body(Map.of("message", "후기가 삭제되었습니다."));
    }// end of public ResponseEntity<Map<String, String>> deleteReview(@PathVariable("id") Long id) -----------------------


    // 한 개 리뷰 조회
    @GetMapping("/api/reviews/{id}")
    public ResponseEntity<ReviewDetailResponse> getReivewDetail(@PathVariable("id") Long id) {
        Review review = reviewService.findById(id);

        return ResponseEntity.status(HttpStatus.OK).body(new ReviewDetailResponse(review));
    }// end of public ResponseEntity<ReviewDetailResponse> getReivewDetail(@PathVariable("id") Long id) ------------------

}
