package io.github.escapehonbab.controller;

import io.ebean.Database;
import io.github.escapehonbab.controller.objects.ResponseBundle;
import io.github.escapehonbab.jpa.DatabaseHandler;
import io.github.escapehonbab.jpa.objects.User;
import io.github.escapehonbab.lang.StaticMessage;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/v1/user")
public class UserManagementController {

    @PostMapping(value = "/register")
    public ResponseBundle registerUser(@RequestBody User user){
        DatabaseHandler handler = DatabaseHandler.getInstance();
        int responseCode = 0;
        String result = null;

        Database database = handler.getDatabase();
        if(database.find(User.class).where().eq("id",user.getId()).exists()){
            responseCode = 400;
            result = StaticMessage.USER_ALREADY_EXISTS;
        }else {
            try{
                database.save(user);
                responseCode = 200;
                result = StaticMessage.SUCCESS_TRANSACTION;
            }catch (Exception ex){
                responseCode = 400;
                result = StaticMessage.ERROR_TRANSACTION;
            }
        }

        return ResponseBundle.builder().response(result).responseCode(responseCode).build();
    }

    @PostMapping("/unreigster")
    public ResponseBundle unregisterUser(@RequestBody User user){
        DatabaseHandler handler = DatabaseHandler.getInstance();
        int responseCode;
        String result;

        Database database = handler.getDatabase();

        if(! database.find(User.class).where().eq("id",user.getId()).exists()){
            responseCode = 400;
            result = StaticMessage.ERROR_NO_USER_FOUND;
        }else{
            try{
                database.delete(user);
                responseCode = 200;
                result = StaticMessage.SUCCESS_TRANSACTION;
            }catch (Exception ex){
                responseCode = 400;
                result = StaticMessage.ERROR_TRANSACTION;
            }
        }
        return ResponseBundle.builder().response(result).responseCode(responseCode).build();
    }

}
