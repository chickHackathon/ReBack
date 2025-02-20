package com.example.rechick.study.controller;

import com.example.rechick.common.BaseResponse;
import com.example.rechick.security.CustomUserDetails;
import com.example.rechick.study.dto.req.StudyCreateReq;
import com.example.rechick.study.dto.req.StudyListReq;
import com.example.rechick.study.dto.res.StudyCreateRes;
import com.example.rechick.study.dto.res.StudyListRes;
import com.example.rechick.study.dto.res.StudyMyListRes;
import com.example.rechick.study.service.StudyService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

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
    // 스터디 목록 조회
    @GetMapping("/list")
    public BaseResponse<List<StudyListRes>> list(@RequestBody StudyListReq studyListReq){
        List<StudyListRes> studyList = studyService.list(studyListReq);
        return new BaseResponse<>(studyList);
    }

    // 내가 방장이거나 회원인 스터디 목록 조회
    @GetMapping("/mylist")
    public BaseResponse<List<StudyMyListRes>> getMyStudies(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        List<StudyMyListRes> myStudies = studyService.getMyStudies(customUserDetails);
        return new BaseResponse<>(myStudies);
    }

}
