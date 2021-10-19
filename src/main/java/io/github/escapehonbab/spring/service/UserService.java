package io.github.escapehonbab.spring.service;

import io.github.escapehonbab.jpa.Role;
import io.github.escapehonbab.jpa.objects.User;
import io.github.escapehonbab.spring.repo.UserRepository;
import lombok.Builder;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Builder
@Service
public class UserService implements UserDetailsService {

    private UserRepository repository;
    private PasswordEncoder encoder;


    public Optional<User> findById(Long id) {
        return repository.findById(id);
    }

    public Optional<User> findByName(String name) {
        return repository.findUserByName(name);
    }

    public Optional<User> findByUserId(String userId) {
        return repository.findUserByUserId(userId);
    }

    public List<User> findAllByNickName(String nickName) {
        return repository.findByNickName(nickName);
    }

    public void deleteAll() {
        repository.deleteAll();
    }

    public void deleteById(Long id) {
        repository.deleteById(id);
    }

    public User save(User user) {
        return repository.save(user);
    }

    public void updateById(Long id, User user) {
        Optional<User> u = repository.findById(id);
        if (u.isPresent()) {
            u.get().setName(user.getName());
            u.get().setUserId(user.getUserId());
            u.get().setPassword(user.getPassword());
            u.get().setNickName(user.getNickName());
            u.get().setAge(user.getAge());
            u.get().setFriends(user.getFriends());
            u.get().setImage(user.getImage());
            u.get().setInterests(user.getInterests());
            u.get().setPhoneNumber(user.getPhoneNumber());
            repository.save(user);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String userId) throws UsernameNotFoundException {
        User user = repository.findUserByUserId(userId).orElseThrow(() -> new UsernameNotFoundException(userId));
        Set<GrantedAuthority> authorities = new HashSet<>();
        authorities.add(new SimpleGrantedAuthority(Role.USER.getValue()));
        if (user.getUserId().equals("singlerr")) {
            authorities.add(new SimpleGrantedAuthority(Role.ADMIN.getValue()));
        }
        return new org.springframework.security.core.userdetails.User(user.getUserId(), user.getPassword(), authorities);
    }

    public User authenticateByUserIdAndPassword(String userId, String password) {
        User user = repository.findUserByUserId(userId).orElseThrow(() -> new UsernameNotFoundException(userId));
        if (!encoder.matches(password, user.getPassword())) {
            throw new BadCredentialsException("Password are not matched");
        }
        return user;
    }
}
