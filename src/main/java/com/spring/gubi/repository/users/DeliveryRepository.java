package com.spring.gubi.repository.users;

import com.spring.gubi.domain.users.Delivery;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

public interface DeliveryRepository extends JpaRepository<Delivery, Long> {

    @Query("SELECT d FROM Delivery d WHERE d.id = :deliveryNo AND d.user.id = :userNo")
    Optional<Delivery> findByIdAndUser_Id(Long deliveryNo, Long userNo);

}//end of interface...
