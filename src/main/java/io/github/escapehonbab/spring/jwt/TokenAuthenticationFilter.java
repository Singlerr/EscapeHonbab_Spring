package io.github.escapehonbab.spring.jwt;

import io.github.escapehonbab.spring.jwt.errors.UnauthorizedException;
import io.github.escapehonbab.spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.transaction.Transactional;
import java.io.IOException;


public class TokenAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserService service;

    @Autowired
    private AuthenticationTokenProvider authenticationTokenProvider;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getRequestURI().equals("/api/v1/user/register") || request.getRequestURI().equals("/api/v1/user/login")
                || request.getRequestURI().equals("/api/v1/auth/verify") || request.getRequestURI().equals("/api/v1/auth/msg") || request.getRequestURI().contains("/api/v1/img");
    }

    @Transactional
    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String token = authenticationTokenProvider.parseTokenString(httpServletRequest);
        if (authenticationTokenProvider.validateToken(token)) {
            Long userNo = authenticationTokenProvider.getTokenOwnerNo(token);
            UserDetails userDetails = service.loadUserByUsername(String.valueOf(userNo));
            try {
                JWTAuthentication authentication = new JWTAuthentication(service, userDetails.getUsername(), userDetails.getPassword());
                authentication.setAuthenticated(true);
                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                SecurityContextHolder.getContext().setAuthentication(authentication);

            } catch (UsernameNotFoundException ex) {
                throw new UnauthorizedException("No user found for " + userNo);
            }
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } else {
            throw new UnauthorizedException("No token found");
        }
    }
}
