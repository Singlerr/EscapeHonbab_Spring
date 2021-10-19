package io.github.escapehonbab.spring.service;

import io.github.escapehonbab.jpa.objects.FriendChatInfo;
import io.github.escapehonbab.spring.repo.ChatInfoRepository;
import lombok.Builder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Builder
@Service
public class ChatInfoService {

    private ChatInfoRepository repository;

    public List<FriendChatInfo> findAll() {
        return repository.findAll();
    }

    public Optional<FriendChatInfo> findById(Long id) {
        return repository.findById(id);
    }

    public List<FriendChatInfo> findAllByOwnerId(Long ownerId) {
        return repository.findFriendChatInfosByOwnerId(ownerId);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public FriendChatInfo save(FriendChatInfo info) {
        return repository.save(info);
    }

    public void updateById(Long id, FriendChatInfo info) {
        Optional<FriendChatInfo> r = repository.findById(id);
        if (r.isPresent()) {
            r.get().setAge(info.getAge());
            r.get().setUserId(info.getUserId());
            r.get().setSex(info.getSex());
            r.get().setImg(info.getImg());
            r.get().setOwnerId(info.getOwnerId());
            r.get().setTime(info.getTime());
            repository.save(info);
        }
    }
}
