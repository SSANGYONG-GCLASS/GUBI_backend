package com.spring.gubi.repository.reviews;


import com.spring.gubi.domain.reviews.Review;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
}
