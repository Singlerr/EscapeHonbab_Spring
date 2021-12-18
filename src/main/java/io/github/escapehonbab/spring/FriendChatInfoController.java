package io.github.escapehonbab.spring;

import io.github.escapehonbab.jpa.objects.FriendChatInfo;
import io.github.escapehonbab.jpa.objects.User;
import io.github.escapehonbab.spring.objects.FriendChatInfoList;
import io.github.escapehonbab.spring.objects.RequestBundle;
import io.github.escapehonbab.spring.service.ChatInfoService;
import io.github.escapehonbab.spring.service.UserService;
import lombok.Builder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.util.AbstractMap;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

@Builder
@EnableAsync
@RestController
@RequestMapping("/api/v1/chat")
public class FriendChatInfoController {

    private ChatInfoService service;
    private UserService userService;


    @Async
    @RequestMapping(value = "/info", method = {RequestMethod.GET, RequestMethod.POST})
    public HashMap<String, FriendChatInfo> getFriendChatInfoByOwnerIdAndUserId(@RequestBody RequestBundle bundle) throws IOException {
        String[] tokens = bundle.getMessage(String[].class);
        return service.findAllByOwnerIdAndUserId(Arrays.stream(tokens).map(t -> new AbstractMap.SimpleEntry<String, String>(t.split(",")[0], t.split(",")[1])).collect(Collectors.toList()));
    }

    @Async
    @RequestMapping(value = "/friend", method = {RequestMethod.GET, RequestMethod.POST})
    public FriendChatInfoList getFriendChatInfoList(@RequestBody RequestBundle bundle) throws IOException {
        return new FriendChatInfoList(service.findAllByOwnerId(bundle.getMessage(User.class).getUserId()));
    }

    @Async
    @RequestMapping(value = "/matched", method = {RequestMethod.GET, RequestMethod.POST})
    public FriendChatInfoList getMatchedChatInfoList(@RequestBody RequestBundle bundle) throws IOException {
        User user = bundle.getMessage(User.class);
        User db = userService.findByUserId(user.getUserId()).orElseThrow(() -> new UsernameNotFoundException("No user found for " + user.getUserId()));
        List<FriendChatInfo> chats = service.findAllByOwnerId(user.getUserId());
        return new FriendChatInfoList(chats.stream().filter(c -> db.getFriends().contains(c.getUserId())).collect(Collectors.toList()));
    }


}
