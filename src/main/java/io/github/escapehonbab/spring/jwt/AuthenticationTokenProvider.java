package io.github.escapehonbab.spring.jwt;

import javax.servlet.http.HttpServletRequest;

public interface AuthenticationTokenProvider {
    String parseTokenString(HttpServletRequest request);

    AuthenticationToken issue(Long userNo);

    Long getTokenOwnerNo(String token);

    boolean validateToken(String token);
}
