package com.spring.gubi.dto.reviews;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateReviewRequest {

    /**
     *  리뷰 수정 요청 DTO
     */

    private Long id;        // 리뷰 번호
    private String title;   // 리뷰 제목
    private String content; // 리뷰 내용
    private int score;      // 별점
    private String img;     // 첨부 이미지
}
