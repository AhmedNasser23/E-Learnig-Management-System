package com.project.LMS.dto.authentication;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class AuthenticationResponse {
    private String token;

    public String getToken() {
        return token;
    }
    public void setToken(String token) {
        this.token = token;
    }
    public static class AuthenticationResponseBuilder {
        private String token;
        public AuthenticationResponseBuilder token(String token) {
            this.token = token;
            return this;
        }
        public AuthenticationResponse build() {
            return new AuthenticationResponse(token);
        }
    }
    public static AuthenticationResponseBuilder builder() {
        return new AuthenticationResponseBuilder();
    }
}