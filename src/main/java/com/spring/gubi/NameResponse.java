package com.spring.gubi;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NameResponse {

    // API 요청 응답 객체
    private Long id;
    private String name;

    public NameResponse(Test test) {
        this.id = test.getId();
        this.name = test.getName();
    }

    @Override
    public String toString() {
        return "NameResponse{" +
                "id=" + id +
                ", name='" + name + '\'' +
                '}';
    }
}
