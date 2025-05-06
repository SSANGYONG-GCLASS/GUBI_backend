package com.spring.gubi.domain.reviews;

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


    @Column(name = "fk_user_no", nullable = false)
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    private User user;  // 회원번호

    @Column(name = "fk_optionno", nullable = false)
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
}
