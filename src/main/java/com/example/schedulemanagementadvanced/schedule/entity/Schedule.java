package com.example.schedulemanagementadvanced.schedule.entity;

import com.example.schedulemanagementadvanced.common.entity.BaseEntity;
import com.example.schedulemanagementadvanced.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "schedules")
@NoArgsConstructor
public class Schedule extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 10) // 10글자 제한
    private String title;

    @Column(columnDefinition = "TEXT") // 내용은  TEXT 타입으로 지정
    private String content;

    // 단방향 연관관계 (N:1)
    // 일정(N) 입장에서 유저(1)는 하나이므로 다음과 같이 정리
    @ManyToOne(fetch = FetchType.LAZY) // 1. 지연 로딩(LAZY)을 사용
    @JoinColumn(name = "user_id")      // 2. 외래키(FK) 컬럼명을 'user_id' 로 지정
    private User user;

    public Schedule(String title, String content, User user) {
        this.title = title;
        this.content = content;
        this.user = user;
    }

    // 비즈니스 로직: 일정 수정 (Dirty Checking 용)
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }
}
