package io.github.escapehonbab.controller;

import io.ebean.Database;
import io.github.escapehonbab.controller.cipher.CipherBase;
import io.github.escapehonbab.controller.jwt.AuthenticationToken;
import io.github.escapehonbab.controller.jwt.JWTAuthenticationTokenProvider;
import io.github.escapehonbab.controller.objects.ResponseBundle;
import io.github.escapehonbab.jpa.DatabaseHandler;
import io.github.escapehonbab.jpa.objects.User;
import io.github.escapehonbab.lang.StaticMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;


@EnableAsync
@RestController
@RequestMapping("/api/v1/user")
public class UserManagementController{

    private JWTAuthenticationTokenProvider authenticationTokenProvider;

    public UserManagementController(JWTAuthenticationTokenProvider tokenProvider){
        this.authenticationTokenProvider = tokenProvider;
    }
    @Async
    @PostMapping(value = "/register")
    public ResponseBundle registerUser(@RequestBody User user) {
        System.out.println("ddd");
        DatabaseHandler handler = DatabaseHandler.getInstance();
        int responseCode = 0;
        String result = null;
        Database database = handler.getDatabase();
        if (database.find(User.class).where().eq("userId", user.getUserId()).exists()) {
            responseCode = StaticMessage.RESP_FAILED;
            result = StaticMessage.USER_ALREADY_EXISTS;
        } else {
            try {
                //String encryptedPass = CipherBase.getInstance().encode(user.getPassword());
                database.save(user);
                AuthenticationToken token = authenticationTokenProvider.issue(user.getId());
                user.setPassword(token.getToken());
                responseCode = StaticMessage.RESP_SUCCESS;
                result = StaticMessage.SUCCESS_TRANSACTION;
            } catch (Exception ex) {
                responseCode = StaticMessage.RESP_FAILED;
                result = StaticMessage.ERROR_TRANSACTION;
            }
        }
        return ResponseBundle.builder().response(result).responseCode(responseCode).build();
    }

    @Async
    @PostMapping("/unreigster")
    public ResponseBundle unregisterUser(@RequestBody User user) {
        DatabaseHandler handler = DatabaseHandler.getInstance();
        int responseCode;
        String result;

        Database database = handler.getDatabase();

        if (!database.find(User.class).where().eq("userId", user.getUserId()).exists()) {
            responseCode = StaticMessage.RESP_FAILED;
            result = StaticMessage.ERROR_NO_USER_FOUND;
        } else {
            try {
                database.delete(user);
                responseCode = StaticMessage.RESP_SUCCESS;
                result = StaticMessage.SUCCESS_TRANSACTION;
            } catch (Exception ex) {
                responseCode = StaticMessage.RESP_FAILED;
                result = StaticMessage.ERROR_TRANSACTION;
            }
        }
        return ResponseBundle.builder().response(result).responseCode(responseCode).build();
    }

    @PostMapping("/login")
    public User login(@RequestBody User user) {
        DatabaseHandler handler = DatabaseHandler.getInstance();
        int responseCode;
        String result;

        Database database = handler.getDatabase();
        if (!database.find(User.class).where().eq("userId", user.getUserId()).exists()) {
            responseCode = StaticMessage.RESP_FAILED;
            result = StaticMessage.ERROR_NO_USER_FOUND;
        } else {
            try {
                User target = database.find(User.class).where().eq("userId", user.getUserId()).findOne();
                if (CipherBase.getInstance().encode(user.getPassword()).equals(target.getPassword())) {
                    result = StaticMessage.SUCCESS_TRANSACTION;
                    responseCode = StaticMessage.RESP_SUCCESS;
                    AuthenticationToken token = authenticationTokenProvider.issue(target.getId());
                    user.setPassword(token.getToken());
                } else {
                    responseCode = StaticMessage.RESP_FAILED;
                    result = StaticMessage.ERROR_LOGIN_FAILED;
                }
            } catch (Exception ex) {
                responseCode = StaticMessage.RESP_FAILED;
                result = StaticMessage.ERROR_TRANSACTION;
            }
        }
        user.setResult(result);
        user.setResponseCode(responseCode);
        return user;
    }
}
