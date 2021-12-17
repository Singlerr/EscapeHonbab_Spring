package io.github.escapehonbab.spring.repo;

import io.github.escapehonbab.jpa.objects.FriendChatInfo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ChatInfoRepository extends JpaRepository<FriendChatInfo, Long> {

    Optional<FriendChatInfo> findFriendChatInfoByOwnerIdAndUserId(String ownerId, String userId);


    List<FriendChatInfo> findFriendChatInfosByOwnerId(String userId);

}
