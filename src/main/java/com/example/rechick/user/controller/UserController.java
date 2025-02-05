package com.example.rechick.user.controller;

import com.example.rechick.common.BaseResponse;
import com.example.rechick.jwt.JwtUtil;
import com.example.rechick.user.dto.req.UserSignupReq;
import com.example.rechick.user.dto.res.UserSignupRes;
import com.example.rechick.user.service.UserService;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.concurrent.TimeUnit;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final RedisTemplate<String, String> redisTemplate;
    private final JwtUtil jwtUtil;

    // 회원가입
    @PostMapping("/signup")
    public BaseResponse<UserSignupRes> signup(@RequestBody UserSignupReq userSignupReq) {
        userService.signup(userSignupReq);
        return new BaseResponse<>();
    }
    @PostMapping("/logout")
    public ResponseEntity<String> logout(@CookieValue(name = "ATOKEN", required = false) String token, HttpServletResponse response) {
        System.out.println("Token received: " + token);  // token 값 확인용
        if (token != null && jwtUtil.isExpired(token) == false) {
            long expirationTime = jwtUtil.getExpirationTime(token);
            System.out.println("Expiration Time: " + expirationTime);  // 만료 시간 확인용
            redisTemplate.opsForValue().set(token, "blacklisted", expirationTime, TimeUnit.MILLISECONDS);

            // 쿠키 삭제 (클라이언트가 더 이상 JWT를 보내지 않도록)
            Cookie expiredCookie = jwtUtil.removeCookie();
            response.addCookie(expiredCookie);
        } else {
            System.out.println("Token is either null or expired");  // 토큰이 없거나 만료된 경우
        }

        return ResponseEntity.ok("Logged out successfully");
    }

}
