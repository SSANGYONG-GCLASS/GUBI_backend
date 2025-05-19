package com.spring.gubi.repository.carts;

import com.spring.gubi.domain.carts.Cart;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    @Query("SELECT c FROM Cart c JOIN FETCH c.option o JOIN FETCH o.product p WHERE c.user.id = :userNo")
    Page<Cart> findByUser_Id(Long userNo, Pageable pageable);

    @Query("SELECT c FROM Cart c WHERE c.user.id = :userNo AND c.option.id = :optionNo")
    Optional<Cart> findByUser_IdAndOption_Id(Long userNo, Long optionNo);

    @Query("SELECT c FROM Cart c JOIN FETCH c.option o JOIN FETCH o.product p WHERE c.id IN :cartNoList AND c.user.id = :userNo")
    List<Cart> findByIdInAndUser_Id(List<Long> cartNoList, Long userNo);

    @Query("SELECT c FROM Cart c JOIN FETCH c.option o WHERE c.id = :id AND c.user.id = :userNo")
    Optional<Cart> findByIdAndUser_Id(Long id, Long userNo);
}
