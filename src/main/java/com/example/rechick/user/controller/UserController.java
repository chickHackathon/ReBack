package com.example.rechick.user.controller;

import com.example.rechick.common.BaseResponse;
import com.example.rechick.user.dto.req.UserSignupReq;
import com.example.rechick.user.dto.res.UserSignupRes;
import com.example.rechick.user.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;

    // 회원가입
    @PostMapping("/signup")
    public BaseResponse<UserSignupRes> signup(@RequestBody UserSignupReq userSignupReq) {
        userService.signup(userSignupReq);
        return new BaseResponse<>();
    }


}
