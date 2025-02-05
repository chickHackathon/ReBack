package com.example.rechick.security;

import com.example.rechick.common.BaseResponseStatus;
import com.example.rechick.exception.custom.InvalidUserException;
import com.example.rechick.user.entity.User;
import com.example.rechick.user.repository.UserRepository;
import com.example.rechick.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CustomUserDetailService implements UserDetailsService {
    private final UserRepository userRepository;
    private final UserService userService;

    @Override
    public UserDetails loadUserByUsername(String username) throws InvalidUserException {
        if (username == null || username.isEmpty()) {
            throw new InvalidUserException(BaseResponseStatus.USER_EMAIL_NULL);
        }
        User user = userRepository.findByEmail(username)
                .orElse(null);

        return new CustomUserDetails(user);
    }
}
