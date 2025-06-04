package com.spring.gubi.dto.users;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@AllArgsConstructor
public class RegisterResponse {
	
    private String accessToken;
    private String refreshToken;
    private UserInfo user;

    @Getter
    @Builder
    @AllArgsConstructor
    public static class UserInfo {
        private String userid;
        private String name;
        private String role;
    }
}
