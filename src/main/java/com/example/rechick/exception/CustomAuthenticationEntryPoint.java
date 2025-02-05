package com.example.rechick.exception;


import com.example.rechick.common.BaseResponse;
import com.example.rechick.common.BaseResponseStatus;
import com.google.gson.Gson;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.AuthenticationEntryPoint;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.io.PrintWriter;

// 스프링 시큐리티에서 인증되지 않은 사용자가 보호된 리소스에 접근하려고 할때 호출되는 인증 실패 처리 로직
// 인증이 필요한 요청에서 인증되지 않은 상태일 경우 적절한 http 응답을 반환하는 역할을 함
@Component
public class CustomAuthenticationEntryPoint implements AuthenticationEntryPoint {
    private final Gson gson = new Gson(); // json 데이터 생성

    // 스프링 시큐리티가 인증되지 않은 요청을 감지했을 때 호출
    @Override
    public void commence(HttpServletRequest request, HttpServletResponse response, AuthenticationException authException) throws IOException {
        response.setCharacterEncoding("UTF-8"); // 응답 문자 인코딩
        response.setContentType("application/json; charset=UTF-8");
        PrintWriter out = response.getWriter(); // printwriter객체를 생성해서 http 응답 본문(body)에 데이터 씀
        String jsonResponse = gson.toJson(new BaseResponse<>(BaseResponseStatus.USER_NOT_LOGIN));
        out.print(jsonResponse);
    }
}
