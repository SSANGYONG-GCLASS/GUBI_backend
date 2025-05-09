package com.spring.gubi.dto.reviews;

import com.spring.gubi.domain.product.Option;
import com.spring.gubi.domain.reviews.Review;
import com.spring.gubi.domain.users.User;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WriteReviewRequest {

    /**
     *  리뷰 작성 요청 DTO
     */

    private Long userNo;    // 회원 번호
    private Long optionNo;  // 옵션 번호
    private String title;   // 리뷰 제목
    private String content; // 리뷰 내용
    private int score;      // 별점
    private String img;     // 첨부 이미지


    public Review toEntity(User user, Option option) {
        return Review.builder()
                .user(user)
                .option(option)
                .title(title)
                .content(content)
                .score(score)
                .img(img)
                .build();
    }
}
