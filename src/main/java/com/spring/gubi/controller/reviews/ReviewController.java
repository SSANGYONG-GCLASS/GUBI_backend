package com.spring.gubi.controller.reviews;

import com.spring.gubi.dto.reviews.WriteReviewRequest;
import com.spring.gubi.dto.reviews.WriteReviewResponse;
import com.spring.gubi.service.reviews.ReviewService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

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
    }
}
