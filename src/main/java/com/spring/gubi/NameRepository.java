package com.spring.gubi;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface NameRepository extends JpaRepository<Test, Long> {


    Optional<Test> findByName(String name);
}
