package io.github.escapehonbab.spring;

import io.github.escapehonbab.jpa.objects.User;
import io.github.escapehonbab.spring.jwt.AuthenticationToken;
import io.github.escapehonbab.spring.jwt.JWTAuthenticationTokenProvider;
import io.github.escapehonbab.spring.objects.ResponseBundle;
import io.github.escapehonbab.spring.service.UserService;
import lombok.Builder;
import org.springframework.http.HttpStatus;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Builder
@EnableAsync
@RestController
@RequestMapping("/api/v1/user")
public class UserController {

    private final JWTAuthenticationTokenProvider authenticationTokenProvider;
    private UserService service;

    @Async
    @PostMapping(value = "/register")
    public ResponseBundle registerUser(@RequestBody User user) {
        User saved = service.save(user);
        return ResponseBundle.builder().response(authenticationTokenProvider.issue(saved.getId()).getToken()).responseCode(HttpStatus.OK.value()).build();
    }

    @Async
    @PostMapping("/unreigster")
    public ResponseBundle unregisterUser(@RequestBody User user) {
        service.deleteById(user.getId());
        return ResponseBundle.builder().response(HttpStatus.OK.toString()).responseCode(HttpStatus.OK.value()).build();
    }

    @PostMapping("/login")
    public User login(@RequestBody User user) {
        User u = service.authenticateByUserIdAndPassword(user.getUserId(), user.getPassword());
        AuthenticationToken token = authenticationTokenProvider.issue(u.getId());
        u.setPassword(token.getToken());
        return u;
    }
}
