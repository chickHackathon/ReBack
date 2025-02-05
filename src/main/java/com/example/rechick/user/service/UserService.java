package com.example.rechick.user.service;

import com.example.rechick.config.RandomNumberGenerator;
import com.example.rechick.user.dto.req.UserSignupReq;
import com.example.rechick.user.entity.User;
import com.example.rechick.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public User signup(UserSignupReq req) {
        // 랜덤 아바타 숫자 생성
        int randomAvatar = RandomNumberGenerator.generateRandomAvatar();

        // 비밀번호 암호화
        String hashedPassword = passwordEncoder.encode(req.getPassword());

        // User 객체 생성
        User user = User.builder()
                .username(req.getName())
                .password(hashedPassword) // 암호화된 비밀번호 저장
                .nickname(req.getNickname())
                .email(req.getEmail())
                .latitude(req.getLatitude())
                .longitude(req.getLonitude())
                .location(req.getLocation())
                .avatar(randomAvatar)
                .build();

        // 데이터베이스에 저장
        return userRepository.save(user);
    }
}

