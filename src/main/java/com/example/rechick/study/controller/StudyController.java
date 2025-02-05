package com.example.rechick.study.controller;

import com.example.rechick.common.BaseResponse;
import com.example.rechick.security.CustomUserDetails;
import com.example.rechick.study.dto.req.StudyCreateReq;
import com.example.rechick.study.dto.res.StudyCreateRes;
import com.example.rechick.study.service.StudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/study")
public class StudyController {
    private final StudyService studyService;

    // 스터디 생성
    @PostMapping()
    public BaseResponse<StudyCreateRes> register(@RequestBody StudyCreateReq studyCreateReq, @AuthenticationPrincipal CustomUserDetails customUserDetails){
        StudyCreateRes studyCreateRes = studyService.register(studyCreateReq, customUserDetails);
        return new BaseResponse<>(studyCreateRes);
    }

}
