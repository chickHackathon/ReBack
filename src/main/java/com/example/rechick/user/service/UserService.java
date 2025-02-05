package com.example.rechick.user.service;

import com.example.rechick.config.RandomNumberGenerator;
import com.example.rechick.jwt.JwtUtil;
import com.example.rechick.user.dto.req.UserSignupReq;
import com.example.rechick.user.entity.User;
import com.example.rechick.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.concurrent.TimeUnit;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;

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
    public void logout(String token) {
        if (token != null && !jwtUtil.isExpired(token)) {
            long expirationTime = jwtUtil.getExpirationTime(token);
            // 블랙리스트에 토큰 저장
            redisTemplate.opsForValue().set(token, "blacklisted", expirationTime, TimeUnit.MILLISECONDS);
        }
    }


}

