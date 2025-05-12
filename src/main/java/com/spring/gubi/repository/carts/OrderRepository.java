package com.spring.gubi.repository.carts;

import com.spring.gubi.domain.orders.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o JOIN FETCH o.delivery JOIN FETCH o.orderDetails od JOIN FETCH od.option op WHERE o.user.id = :id")
    Optional<Page<Order>> findByUser_Id(Long id, Pageable pageable);
}
