package com.spring.gubi.repository.carts;

import com.spring.gubi.domain.carts.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Long> {
    @Query("SELECT c FROM Cart c JOIN FETCH c.option o JOIN FETCH o.product p WHERE c.user.userid = :userId")
    List<Cart> findByUser_Userid(String userId);

    @Query("SELECT c FROM Cart c WHERE c.user.id = :userNo AND c.option.id = :optionNo")
    Optional<Cart> findByUser_IdAndOption_Id(Long userNo, Long optionNo);

    @Query("SELECT c FROM Cart c JOIN FETCH c.option o JOIN FETCH o.product p WHERE c.id IN :cartNoList AND c.user.userid = :userId")
    List<Cart> findByIdInAndUser_Userid(List<Long> cartNoList, String userId);

    @Query("SELECT c FROM Cart c JOIN FETCH c.option o WHERE c.id = :id AND c.user.id = :userNo")
    Optional<Cart> findByIdAndUser_Id(Long id, Long userNo);

    @Query("SELECT c FROM Cart c WHERE c.id = :id AND c.user.userid = :userId")
    Optional<Cart> findByIdAndUser_Userid(Long id, String userId);
}
