package com.spring.gubi.repository.products;

import com.spring.gubi.domain.product.Option;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OptionRepository extends JpaRepository<Option, Long> {
}
