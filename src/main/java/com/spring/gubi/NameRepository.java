package com.spring.gubi;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface NameRepository extends JpaRepository<Test, Long> {


    @Query("SELECT t FROM Test t WHERE t.name = :name")
    Optional<Test> findByName(@Param("name") String name);
}
