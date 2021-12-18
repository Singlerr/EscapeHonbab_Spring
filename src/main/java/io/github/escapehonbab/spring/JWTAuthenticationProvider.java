package io.github.escapehonbab.spring;

import io.github.escapehonbab.spring.jwt.JWTAuthentication;
import io.github.escapehonbab.spring.service.UserService;
import lombok.AllArgsConstructor;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

@AllArgsConstructor
@Component
public class JWTAuthenticationProvider implements AuthenticationProvider {

    private UserService service;

    @Override
    public Authentication authenticate(Authentication authentication) throws BadCredentialsException {
        service.authenticateByUserIdAndPassword(authentication.getPrincipal().toString(), authentication.getCredentials().toString());
        return new JWTAuthentication(service, authentication.getPrincipal().toString(), authentication.getCredentials().toString());
    }

    @Override
    public boolean supports(Class<?> aClass) {
        return true;
    }
}
