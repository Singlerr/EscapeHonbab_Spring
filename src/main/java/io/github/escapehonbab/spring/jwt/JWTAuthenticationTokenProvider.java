package io.github.escapehonbab.spring.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import java.sql.Date;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;

@Component
public class JWTAuthenticationTokenProvider implements AuthenticationTokenProvider {

    private static final String SECRET_KEY = "Vaf6vj6MmVo1NIUbKfk1SfXx3JGdM48B";
    private static final long EXPIRATION_MS = 1000 * 60 * 60 * 24;

    @Override
    public String parseTokenString(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    @Override
    public AuthenticationToken issue(Long userNo) {
        return JWTAuthenticationToken.builder().token(generateToken(userNo)).build();
    }

    private String generateToken(Long userNo) {
        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expired = now.plus(EXPIRATION_MS, ChronoUnit.MILLIS);
        return Jwts.builder()
                .setSubject(Long.toString(userNo))
                .setIssuedAt(Date.from(now.atZone(ZoneId.systemDefault()).toInstant()))
                .setExpiration(Date.from(expired.atZone(ZoneId.systemDefault()).toInstant()))
                .signWith(SignatureAlgorithm.HS512, SECRET_KEY)
                .compact();
    }

    @Override
    public Long getTokenOwnerNo(String token) {
        Claims c = Jwts.parser()
                .setSigningKey(SECRET_KEY)
                .parseClaimsJws(token)
                .getBody();
        return Long.parseLong(c.getSubject());
    }

    @Override
    public boolean validateToken(String token) {
        if (StringUtils.isNotEmpty(token)) {
            try {
                Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(token);
                return true;
            } catch (Exception ex) {

            }
        }
        return false;
    }
}
