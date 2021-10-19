package io.github.escapehonbab.spring.jwt;

import io.github.escapehonbab.jpa.objects.User;
import io.github.escapehonbab.spring.jwt.errors.UnauthorizedException;
import io.github.escapehonbab.spring.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Optional;

public class TokenAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private UserService service;

    @Autowired
    private AuthenticationTokenProvider authenticationTokenProvider;

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) throws ServletException {
        return request.getRequestURI().equals("/api/v1/user/register") || request.getRequestURI().equals("/api/v1/user/login");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, FilterChain filterChain) throws ServletException, IOException {
        String token = authenticationTokenProvider.parseTokenString(httpServletRequest);
        if (authenticationTokenProvider.validateToken(token)) {
            Long userNo = authenticationTokenProvider.getTokenOwnerNo(token);

            Optional<User> u = service.findById(userNo);
            if (u.isPresent()) {
                User member = u.get();
                try {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(member, member.getPassword());
                    authenticationToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(httpServletRequest));
                    SecurityContextHolder.getContext().setAuthentication(authenticationToken);
                } catch (UsernameNotFoundException ex) {
                    throw new UnauthorizedException("No user found for " + userNo);
                }
            } else {
                throw new UnauthorizedException("No user found for " + userNo);
            }
            filterChain.doFilter(httpServletRequest, httpServletResponse);
        } else {
            throw new UnauthorizedException("No token found");
        }
    }
}
