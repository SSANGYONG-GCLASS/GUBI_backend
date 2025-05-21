package com.spring.gubi.repository.products;

import com.spring.gubi.domain.carts.Cart;
import com.spring.gubi.domain.product.Option;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface OptionRepository extends JpaRepository<Option, Long> {
}
