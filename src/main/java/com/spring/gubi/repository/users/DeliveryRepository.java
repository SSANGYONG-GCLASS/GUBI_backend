package com.spring.gubi.repository.users;

import com.spring.gubi.domain.users.Delivery;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    @Query("SELECT d FROM Delivery d WHERE d.id = :deliveryNo AND d.user.id = :userNo")
    Optional<Delivery> findByIdAndUser_Id(Long deliveryNo, Long userNo);

    // 페이저블 객체를 넣으면 페이지 객체 형태로 반환해준다!
    @Query("SELECT d FROM Delivery d JOIN FETCH d.user u WHERE d.user.id = :userNo")
    Page<Delivery> findByUser_Id(Long userNo, Pageable pageable);
    
    
}//end of interface...
