package com.spring.gubi.repository.carts;

import com.spring.gubi.domain.orders.Order;
import com.spring.gubi.domain.orders.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {

    @Query("SELECT o FROM Order o JOIN FETCH o.delivery JOIN FETCH o.orderDetails od JOIN FETCH od.option op WHERE o.user.id = :id AND o.status IN :statuses")
    Optional<Page<Order>> findByUser_IdAndStatusIn(Long id, List<OrderStatus> statuses, Pageable pageable);
}
