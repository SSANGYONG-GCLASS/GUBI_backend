package com.spring.gubi;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;

@Entity
@Getter
public class Test {

    @Id
    private Long id;
    private String name;
}
