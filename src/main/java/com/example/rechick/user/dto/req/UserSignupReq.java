package com.example.rechick.user.dto.req;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class UserSignupReq {
    private String name;
    private String nickname;
    private String email;
    private String password;
    private String location;
    private float latitude;
    private float lonitude;
}
