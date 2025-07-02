package com.spring.gubi.scheduler;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import com.spring.gubi.domain.users.User;
import com.spring.gubi.domain.users.UserStatus;
import com.spring.gubi.repository.users.UserRepository;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class UserIdleStatusScheduler {

    private final UserRepository userRepository;

    // 매일 새벽 3시에 실행 (cron 표현식)
    @Scheduled(cron = "0 0 3 * * ?")
    public void updateIdleUsers() {
        log.info("스케줄러 실행 - 12개월 이상 로그인 없는 사용자 상태 변경 시작");

        // 12개월 이상 로그인 기록이 없는, 현재 ACTIVE 상태인 사용자 조회
        List<User> usersToIdle = userRepository.findAllByStatusAndLastloginatBefore(
            UserStatus.ACTIVE, LocalDateTime.now().minusMonths(12)
        );

        if (usersToIdle.isEmpty()) {
            log.info("대상 사용자 없음");
            return;
        }

        for (User user : usersToIdle) {
            user.updateStatus(UserStatus.IDLE); // 상태 변경 메서드 호출
            log.info("사용자 ID: {} 상태를 IDLE로 변경", user.getUserid());
        }

        userRepository.saveAll(usersToIdle);

        log.info("스케줄러 종료 - 상태 변경 완료");
    }
}
