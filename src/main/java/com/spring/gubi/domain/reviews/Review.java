package com.spring.gubi.domain.reviews;

import com.spring.gubi.domain.product.Option;
import com.spring.gubi.domain.users.User;
import com.spring.gubi.dto.reviews.UpdateReviewRequest;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "reviews")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@Builder
public class Review {

    @Id
    @Column(name = "reviewno", unique = true, nullable = false, updatable = false)
    @SequenceGenerator(name = "SEQ_REVIEW_GENERATOR", sequenceName = "review_seq", allocationSize = 1)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "SEQ_REVIEW_GENERATOR")
    private Long id;    // 리뷰번호


    @JoinColumn(name = "fk_user_no", referencedColumnName = "user_no", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private User user;  // 회원번호

    @JoinColumn(name = "fk_optionno", referencedColumnName = "optionno", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY)
    private Option option; // 옵션번호

    @Column(name = "title", nullable = false)
    private String title;   // 제목

    @Column(name = "content", nullable = false)
    private String content; // 본문

    @Column(name = "score", nullable = false)
    private Integer score;  // 별점

    @Column(name = "registerday")
    @Builder.Default
    private LocalDateTime registerDay = LocalDateTime.now();    // 작성일

    @Column(name = "img")
    private String img;     // 리뷰 이미지



    // 리뷰 수정 메소드
    public void updateReview(UpdateReviewRequest request) {
        this.title = request.getTitle();
        this.content = request.getContent();
        this.score = request.getScore();
    }

    // 리뷰 이미지 수정 메소드
    public void updateImg(String img) {
        this.img = img;
    }
}
