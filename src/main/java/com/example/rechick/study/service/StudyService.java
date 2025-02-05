package com.example.rechick.study.service;

import com.example.rechick.category.Category;
import com.example.rechick.security.CustomUserDetails;
import com.example.rechick.study.dto.req.StudyCreateReq;
import com.example.rechick.study.dto.res.StudyCreateRes;
import com.example.rechick.study.entity.Study;
import com.example.rechick.study.repository.StudyRepository;
import com.example.rechick.user.entity.User;
import com.example.rechick.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class StudyService {
    private final UserRepository userRepository;
    private final StudyRepository studyRepository;

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
                .studyTime(studyCreateReq.getStudyTime())
                .category(Category.valueOf(studyCreateReq.getCategory()))
                .deadline(studyCreateReq.getDeadline())
                .build();
        studyRepository.save(study);
        return new StudyCreateRes(study.getId(), study.getStudyRoomName());
    }

}
