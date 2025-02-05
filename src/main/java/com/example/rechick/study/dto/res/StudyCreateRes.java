package com.example.rechick.study.dto.res;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;

@Builder
@Getter
@AllArgsConstructor
public class StudyCreateRes {

    private Long studyId;
    private String studyName;
}
