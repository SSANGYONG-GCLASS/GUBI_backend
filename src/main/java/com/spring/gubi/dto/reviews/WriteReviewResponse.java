package com.spring.gubi.dto.reviews;

import com.spring.gubi.domain.reviews.Review;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class WriteReviewResponse {

    /**
     *  리뷰 작성 시 응답 DTO
     */

    private Long id;        // 리뷰 번호
    private Long userNo;    // 회원번호
    private String name;    // 회원명
    private Long optionNo;  // 옵션 번호
    private String optionName;  // 옵션명
    private String optionColor; // 옵션 컬러

    private String title;   // 리뷰 제목
    private String content; // 리뷰 내용
    private Integer score;  // 별점
    private String img;     // 리뷰 이미지


    public WriteReviewResponse (Review review) {
        this.id = review.getId();
        this.userNo = review.getUser().getId();
        this.name = review.getUser().getName();
        this.optionNo = review.getOption().getId();
        this.optionName = review.getOption().getName();
        this.optionColor = review.getOption().getColor();
        this.title = review.getTitle();
        this.content = review.getContent();
        this.score = review.getScore();
        this.img = review.getImg();
    }

    @Override
    public String toString() {
        return "WriteReviewResponse{" +
                "id=" + id +
                ", userNo=" + userNo +
                ", name='" + name + '\'' +
                ", optionNo=" + optionNo +
                ", optionName='" + optionName + '\'' +
                ", optionColor='" + optionColor + '\'' +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", score=" + score +
                ", img='" + img + '\'' +
                '}';
    }
}
