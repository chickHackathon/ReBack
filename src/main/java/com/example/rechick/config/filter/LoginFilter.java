package com.example.rechick.config.filter;


import com.example.rechick.jwt.JwtUtil;
import com.example.rechick.security.CustomUserDetails;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.DisabledException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import java.io.IOException;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class LoginFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtUtil jwtUtil;
    private final AuthenticationManager authenticationManager;

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response) throws AuthenticationException {
        String requestBody;

        try {
            requestBody = request.getReader().lines().collect(Collectors.joining());
        } catch (IOException e) {
            throw new BadCredentialsException("잘못된 요청 본문입니다.");
        }

        ObjectMapper objectMapper = new ObjectMapper();
        Map<String, String> requestData;

        try {
            requestData = objectMapper.readValue(requestBody, new TypeReference<>() {
            });
        } catch (IOException e) {
            throw new BadCredentialsException("JSON 요청 본문을 파싱하는 데 실패했습니다.");
        }

        String username = requestData.get("email");
        String password = requestData.get("password");

        if (username == null || password == null) {
            throw new BadCredentialsException("이메일 또는 비밀번호가 비어있습니다.");
        }

        UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(username, password, null);

        try {
            Authentication authentication = authenticationManager.authenticate(authToken);
            CustomUserDetails userDetails = (CustomUserDetails) authentication.getPrincipal();

            return authentication;
        } catch (BadCredentialsException e) {
            throw new BadCredentialsException(e.getMessage());
        } catch (DisabledException e) {
            throw new DisabledException(e.getMessage());
        }
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest request, HttpServletResponse response,
                                            FilterChain chain, Authentication authResult) throws IOException {
        CustomUserDetails user = (CustomUserDetails) authResult.getPrincipal();
        Collection<String> authorities = authResult.getAuthorities().stream()
                .map(authority -> authority.getAuthority())
                .toList();

        String role = authorities.iterator().next();
        String username = user.getUsername();
        Long userId = user.getUserId();

        // JWT 토큰 생성
        String token = jwtUtil.createToken(userId, username, role);
        Cookie jwtCookie = jwtUtil.createCookie(token);
        response.addCookie(jwtCookie);

        // JSON 응답 설정
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("userId", userId);
        responseData.put("token", token);  // 토큰을 응답에 추가

        ObjectMapper objectMapper = new ObjectMapper();
        response.getWriter().write(objectMapper.writeValueAsString(responseData));
    }
}
