package io.github.escapehonbab.spring.jwt;

import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
public class JWTAuthenticationToken implements AuthenticationToken {
    private String token;

    @Override
    public String getToken() {
        return token;
    }
}
