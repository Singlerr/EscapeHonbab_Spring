package io.github.escapehonbab.spring.jwt;

import io.github.escapehonbab.jpa.Role;
import io.github.escapehonbab.spring.service.UserService;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.HashSet;
import java.util.Set;


public class JWTAuthentication implements Authentication {

    private static final Set<GrantedAuthority> authorities = new HashSet<>();

    static {
        authorities.add(new SimpleGrantedAuthority(Role.USER.getValue()));
    }

    private final UserService userService;
    private final String userId;
    private final String password;
    private boolean authenticated = false;
    private Object details;

    public JWTAuthentication(UserService service, String userId, String password) {
        this.userService = service;
        this.userId = userId;
        this.password = password;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return authorities;
    }

    @Override
    public Object getCredentials() {
        return password;
    }

    @Override
    public Object getDetails() {
        return this.details;
    }

    public void setDetails(Object details) {
        this.details = details;
    }

    @Override
    public Object getPrincipal() {
        return userId;
    }

    @Override
    public boolean isAuthenticated() {
        return authenticated;
    }

    @Override
    public void setAuthenticated(boolean b) throws IllegalArgumentException {
        this.authenticated = b;
    }

    /**
     * Returns the name of this principal.
     *
     * @return the name of this principal.
     */
    @Override
    public String getName() {
        return userId;
    }
}
