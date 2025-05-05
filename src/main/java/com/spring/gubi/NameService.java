package com.spring.gubi;

import org.springframework.stereotype.Service;

@Service
public class NameService {

    // 데이터 조회 테스트

    private final NameRepository nameRepository;

    public NameService(NameRepository nameRepository) {
        this.nameRepository = nameRepository;
    }


    // 이름으로 조회
    public NameResponse findByName(String name) {
        return nameRepository.findByName(name)
                .map(test -> new NameResponse(test))
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이름입니다."));
    }
}
