package io.github.escapehonbab.controller;

import com.google.inject.internal.util.Lists;
import io.github.escapehonbab.controller.objects.FriendChatInfoList;
import io.github.escapehonbab.controller.objects.RestaurantList;
import io.github.escapehonbab.jpa.DatabaseHandler;
import io.github.escapehonbab.jpa.objects.FriendChatInfo;
import io.github.escapehonbab.jpa.objects.Restaurant;
import io.github.escapehonbab.jpa.objects.User;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Optional;

@EnableAsync
@RestController
@RequestMapping("/api/v1/misc")
public class MiscController {

    @Async
    @GetMapping(value = "/restaurant")
    public RestaurantList getRestaurantList() {
        List<Restaurant> list = DatabaseHandler.getInstance().getDatabase().find(Restaurant.class).findList();
        return new RestaurantList(list);
    }

    @Async
    @GetMapping(value = "/friends")
    public FriendChatInfoList getFriendChatInfoList(@RequestBody User user) {
        List<FriendChatInfo> list = Lists.newArrayList();
        for (String id : user.getFriends()) {
            Optional<FriendChatInfo> opt = DatabaseHandler.getInstance().getDatabase().find(FriendChatInfo.class).where().eq("user", user).eq("userId", id).findOneOrEmpty();
            if (!opt.isEmpty())
                list.add(opt.get());
        }
        return new FriendChatInfoList(list);
    }
}
