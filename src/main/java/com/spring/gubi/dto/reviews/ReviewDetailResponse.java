package com.spring.gubi.dto.reviews;

import com.spring.gubi.domain.reviews.Review;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ReviewDetailResponse {

    /**
     *  리뷰 상세 정보를 반환하는 DTO
     */

    private Long id;        // 리뷰 번호
    private String title;   // 제목
    private String content; // 내용
    private int score;      // 별점
    private String img;     // 이미지

    private Long userNo;  // 회원번호
    private String userid;  // 아이디
    private String name;    // 이름


    public ReviewDetailResponse(Review review) {
        this.id = review.getId();
        this.title = review.getTitle();
        this.content = review.getContent();
        this.score = review.getScore();
        this.img = review.getImg();

        this.userNo = review.getUser().getId();
        this.userid = review.getUser().getUserid();
        this.name = review.getUser().getName();
    }

    @Override
    public String toString() {
        return "ReviewDetailResponse{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", content='" + content + '\'' +
                ", score=" + score +
                ", userNo='" + userNo + '\'' +
                ", userid='" + userid + '\'' +
                ", name='" + name + '\'' +
                '}';
    }
}
