package io.github.escapehonbab.spring.repo;

import io.github.escapehonbab.jpa.objects.FriendChatInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ChatInfoRepository extends JpaRepository<FriendChatInfo, Long> {

    FriendChatInfo findByOwnerId(Long id);

    List<FriendChatInfo> findFriendChatInfosByOwnerId(Long id);

}
