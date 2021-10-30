package io.github.escapehonbab.spring;

import io.github.escapehonbab.jpa.objects.FriendChatInfo;
import io.github.escapehonbab.jpa.objects.User;
import io.github.escapehonbab.spring.objects.FriendChatInfoList;
import io.github.escapehonbab.spring.service.ChatInfoService;
import io.github.escapehonbab.spring.service.UserService;
import lombok.Builder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    @GetMapping(value = "/friend")
    public FriendChatInfoList getFriendChatInfoList(@RequestBody User user) {
        return new FriendChatInfoList(service.findAllByOwnerId(user.getId()));
    }

    @Async
    @GetMapping(value = "/matched")
    public FriendChatInfoList getMatchedChatInfoList(@RequestBody User user) {
        User db = userService.findByUserId(user.getUserId()).orElseThrow(() -> new UsernameNotFoundException("No user found for " + user.getUserId()));
        List<FriendChatInfo> chats = service.findAllByOwnerId(user.getId());
        return new FriendChatInfoList(chats.stream().filter(c -> db.getFriends().contains(c.getUserId())).collect(Collectors.toList()));
    }
}
