package io.github.escapehonbab.spring.jwt.errors;

import org.springframework.security.core.AuthenticationException;

public class UnauthorizedException extends AuthenticationException {
    public UnauthorizedException(String msg, Throwable cause) {
        super(msg, cause);
    }

    public UnauthorizedException(String msg) {
        super(msg);
    }
}
