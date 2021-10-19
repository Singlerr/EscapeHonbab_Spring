package io.github.escapehonbab.spring.repo;

import io.github.escapehonbab.jpa.objects.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findUserByUserId(String id);

    Optional<User> findUserByName(String name);

    Optional<User> findUserByPhoneNumber(String phoneNumber);

    List<User> findByNickName(String nickName);

}
