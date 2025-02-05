package com.example.rechick.config.filter;


import com.example.rechick.jwt.JwtUtil;
import com.example.rechick.security.CustomUserDetails;
import com.example.rechick.user.entity.User;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

public class JwtFilter extends OncePerRequestFilter {
    private final JwtUtil jwtUtil;
    private final RedisTemplate<String, String> redisTemplate;

    public JwtFilter(JwtUtil jwtUtil, RedisTemplate<String, String> redisTemplate) {
        this.jwtUtil = jwtUtil;
        this.redisTemplate = redisTemplate;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String authorization = null;

        if (request.getCookies() != null) {
            for (Cookie cookie : request.getCookies()) {
                if (cookie.getName().equals("ATOKEN")) {
                    String token = cookie.getValue();

                    // 블랙리스트 확인: Redis에 저장된 토큰이면 인증 거부
                    if (redisTemplate.hasKey(token)) {
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        return;
                    }

                    // 토큰 만료 확인
                    if (jwtUtil.isExpired(token)) {
                        Cookie expiredCookie = jwtUtil.removeCookie();
                        response.addCookie(expiredCookie);
                        response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
                        return;
                    }

                    authorization = token;
                    break;
                }
            }
        }

        if (authorization == null) {
            filterChain.doFilter(request, response);
            return;
        }

        Long id = jwtUtil.getId(authorization);
        String email = jwtUtil.getEmail(authorization);
        String nickname = jwtUtil.getNickname(authorization);

        User user = User.builder()
                .id(id)
                .email(email)
                .nickname(nickname)
                .build();

        CustomUserDetails customUserDetails = new CustomUserDetails(user);
        Authentication authToken = new UsernamePasswordAuthenticationToken(customUserDetails, null, customUserDetails.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authToken);

        filterChain.doFilter(request, response);
    }
}
