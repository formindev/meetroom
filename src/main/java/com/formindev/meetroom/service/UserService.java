package com.formindev.meetroom.service;

import com.formindev.meetroom.domain.User;
import com.formindev.meetroom.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String userName) throws UsernameNotFoundException {
        User user = userRepository.findByUserName(userName);

        if (user == null)
            throw new UsernameNotFoundException("User not found!");

        return user;
    }

    public User getUserById(Long userId) {
        return userRepository.findById(userId).get();
    }
}
