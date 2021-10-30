package io.github.escapehonbab.spring;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.github.escapehonbab.jpa.objects.User;
import io.github.escapehonbab.lang.StaticMessage;
import io.github.escapehonbab.netty.utils.ObjectSerializer;
import io.github.escapehonbab.spring.jwt.AuthenticationToken;
import io.github.escapehonbab.spring.jwt.JWTAuthenticationTokenProvider;
import io.github.escapehonbab.spring.objects.RequestBundle;
import io.github.escapehonbab.spring.objects.ResponseBundle;
import io.github.escapehonbab.spring.service.UserService;
import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;


@Builder
@EnableAsync
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final JWTAuthenticationTokenProvider authenticationTokenProvider;
    private UserService service;
    private PasswordEncoder encoder;

    @PostMapping(value = "/register")
    public ResponseBundle registerUser(@RequestBody RequestBundle req) throws IOException {
        User user = req.getMessage(User.class);
        user.setPassword(encoder.encode(user.getPassword()));
        if(service.findByUserId(user.getUserId()).isPresent()){
            return ResponseBundle.builder().response(StaticMessage.USER_ALREADY_EXISTS).responseCode(HttpStatus.NOT_ACCEPTABLE.value()).build();
        }
        User saved = service.save(user);
        return ResponseBundle.builder().response(authenticationTokenProvider.issue(saved.getId()).getToken()).responseCode(HttpStatus.OK.value()).build();
    }

    @Async
    @PostMapping("/unreigster")
    public ResponseBundle unregisterUser(@RequestBody RequestBundle req) throws IOException {
        User user = req.getMessage(User.class);
        service.deleteById(user.getId());
        return ResponseBundle.builder().response(HttpStatus.OK.toString()).responseCode(HttpStatus.OK.value()).build();
    }

    @PostMapping("/login")
    public ResponseBundle login(@RequestBody RequestBundle req) throws IOException {
        User user = req.getMessage(User.class);
        User u = service.authenticateByUserIdAndPassword(user.getUserId(), user.getPassword());
        AuthenticationToken token = authenticationTokenProvider.issue(u.getId());
        ResponseBundle bundle = ResponseBundle.builder().build();
        u.setPassword(token.getToken());
        bundle.setMessage(u);
        bundle.setResponseCode(HttpStatus.OK.value());
        return bundle;
    }
}
