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

//        NameResponse nameResponse = null;
//        Test test = nameRepository.findByName(name);
//        if (test != null) {
//            // 여기가 .map
//            nameResponse = new NameResponse(test);
//        }
//        else {
//            // 여기가 orElseThrow
//            throw new IllegalArgumentException("존재하지 않는 이름입니다.");
//        }
//
//        return nameResponse;


        return nameRepository.findByName(name)
                .map(test -> new NameResponse(test))
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 이름입니다."));
    }
}
