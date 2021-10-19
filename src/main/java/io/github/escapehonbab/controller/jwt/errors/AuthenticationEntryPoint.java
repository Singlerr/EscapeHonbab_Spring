package io.github.escapehonbab.controller.jwt.errors;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.github.escapehonbab.controller.objects.ResponseBundle;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Component
public class AuthenticationEntryPoint implements org.springframework.security.web.AuthenticationEntryPoint {
    @Override
    public void commence(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
        ResponseBundle bundle = ResponseBundle.builder().responseCode(HttpStatus.UNAUTHORIZED.value()).response(e.getMessage()).build();
        try (OutputStream os = httpServletResponse.getOutputStream()) {
            ObjectMapper mapper = new ObjectMapper();
            os.write(mapper.writeValueAsBytes(bundle));
            os.flush();
        }
    }
}
