package com.example.rechick.study.service;

import com.example.rechick.category.Category;
import com.example.rechick.common.BaseResponseStatus;
import com.example.rechick.security.CustomUserDetails;
import com.example.rechick.study.dto.req.StudyCreateReq;
import com.example.rechick.study.dto.req.StudyListReq;
import com.example.rechick.study.dto.res.StudyCreateRes;
import com.example.rechick.study.dto.res.StudyListRes;
import com.example.rechick.study.dto.res.StudyMyListRes;
import com.example.rechick.study.entity.Study;
import com.example.rechick.study.repository.StudyRepository;
import com.example.rechick.user.entity.User;
import com.example.rechick.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class StudyService {
    private final UserRepository userRepository;
    private final StudyRepository studyRepository;

    // 생성
    public StudyCreateRes register(StudyCreateReq studyCreateReq, CustomUserDetails customUserDetails){
        // 스터디장
        Long userId = customUserDetails.getUserId();
        User leader = userRepository.findById(userId)
                .orElseThrow(()->new IllegalArgumentException("사용자를 찾을 수 없습니다."));
        Study study = Study.builder()
                .user(leader)
                .studyRoomName(studyCreateReq.getStudyRoomName())
                .title(studyCreateReq.getTitle())
                .content(studyCreateReq.getContent())
                .deadLine(studyCreateReq.getDeadline())
                .studyTime(studyCreateReq.getStudyTime())
                .category(Category.valueOf(studyCreateReq.getCategory()))
                .build();
        studyRepository.save(study);
        return new StudyCreateRes(study.getId(), study.getStudyRoomName());
    }
    // 조회
    public List<StudyListRes> list(StudyListReq studyListReq){
        List<Study> study = studyRepository.findAllByOrderByIdAsc();
        return study.stream()
                .map(entity -> StudyListRes.builder()
                        .id(entity.getId())
                        .title(entity.getTitle())
                        .img(entity.getImg())
                        .content(entity.getContent())
                        .category(String.valueOf(entity.getCategory()))
                        .finish(entity.isFinish())
                        .studyTime(entity.getStudyTime())
                        .location(entity.getLocation())
                        .build())
                .collect(Collectors.toList());
    }
    // 나의 스터디 목록 조회
    public List<StudyMyListRes> getMyStudies(CustomUserDetails customUserDetails) {
        Long userId = customUserDetails.getUserId();

        List<Study> leaderStudies = studyRepository.findByUserId(userId);
        List<Study> memberStudies = studyRepository.findByUsers_Id(userId);

        Set<Study> allStudies = new HashSet<>(leaderStudies);
        allStudies.addAll(memberStudies);

        return allStudies.stream()
                .map(study -> StudyMyListRes.builder()
                        .id(study.getId())
                        .title(study.getTitle())
                        .img(study.getImg())
                        .content(study.getContent())
                        .category(String.valueOf(study.getCategory()))
                        .finish(study.isFinish())
                        .build())
                .collect(Collectors.toList());
    }


}
