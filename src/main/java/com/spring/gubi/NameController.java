package com.spring.gubi;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@Slf4j
public class NameController {

    // REST API 통신 테스트용

    private final NameService nameService;

    public NameController(NameService nameService) {
        this.nameService = nameService;
    }

    // 관리자 이름 조회
    @GetMapping("/api/admin")
    public ResponseEntity<NameResponse> admin() {
        NameResponse name = nameService.findByName("관리자");

        log.info("관리자 정보 요청됨!! 반환정보: {}", name);

        return ResponseEntity.ok().body(name);
    }


    // 유저 이름 조회
    @GetMapping("/api/user")
    public ResponseEntity<NameResponse> user() {
        NameResponse name = nameService.findByName("일반회원");

        log.info("일반회원 정보 요청됨!! 반환정보: {}", name);

        return ResponseEntity.ok().body(name);
    }
}
