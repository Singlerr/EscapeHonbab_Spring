package io.github.escapehonbab.spring;

import io.github.escapehonbab.jpa.objects.User;
import io.github.escapehonbab.spring.objects.FriendChatInfoList;
import io.github.escapehonbab.spring.service.ChatInfoService;
import lombok.Builder;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@Builder
@EnableAsync
@RestController
@RequestMapping("/api/v1/friends")
public class FriendChatInfoController {

    private ChatInfoService service;

    @Async
    @GetMapping(value = "/get")
    public FriendChatInfoList getFriendChatInfoList(@RequestBody User user) {
        return new FriendChatInfoList(service.findAllByOwnerId(user.getId()));
    }
}
