package com.example.rechick.study.repository;

import com.example.rechick.study.entity.Study;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudyRepository extends JpaRepository<Study, Long> {

    // 내가 방장인 스터디 조회
    List<Study> findByUserId(Long userId);

    // 내가 스터디원으로 참여 중인 스터디 조회
    List<Study> findByUsers_Id(Long userId);

    List<Study> findAllByOrderByIdAsc();

}

