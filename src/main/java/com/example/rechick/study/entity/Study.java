package com.example.rechick.study.entity;

import com.example.rechick.category.Category;
import com.example.rechick.user.entity.User;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

import static jakarta.persistence.FetchType.LAZY;

@Entity
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Builder
@EntityListeners(AuditingEntityListener.class)
public class Study {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    //  대표이미지(s3)

    @Enumerated(EnumType.STRING)
    private Category category;

    @Builder.Default
    @Column(name = "Img")
    private String Img = "";

    @ManyToOne(fetch = LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "title", length = 100)
    private String title; // varchar(100)

    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content; // text

    @Column(name = "created_at")
    @CreatedDate
    private LocalDateTime createdAt; // datetime

    @Column(name = "deadline")
    @CreatedDate
    private LocalDateTime deadline; // datetime

    @Column(name = "study_time")
    @CreatedDate
    private LocalDateTime studyTime; // datetime

    @Column(name = "finish")
    private boolean finish;

    private String studyRoomName;

}