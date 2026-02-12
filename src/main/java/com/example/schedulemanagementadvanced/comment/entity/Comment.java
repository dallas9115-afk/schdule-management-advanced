package com.example.schedulemanagementadvanced.comment.entity;

import com.example.schedulemanagementadvanced.common.entity.BaseEntity;
import com.example.schedulemanagementadvanced.schedule.entity.Schedule;
import com.example.schedulemanagementadvanced.user.entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "comments")
@NoArgsConstructor
public class Comment extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    // 유저와의 관계 (N:1)
    // "이 댓글은 누가 썼는가?"
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    // 일정과의 관계 (N:1)
    // "이 댓글은 어떤 일정에 달렸는가?"
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "schedule_id", nullable = false)
    private Schedule schedule;

    // 생성자: 댓글을 만들 때는 '내용', '작성자', '일정'
    public Comment(String content, User user, Schedule schedule) {
        this.content = content;
        this.user = user;
        this.schedule = schedule;
    }

    // 업데이트 시에는 Setter 대신 update 활용
    public void update(String content) {
        this.content = content;
    }

    // 수정 로직: 댓글 내용 수정
    public void updateContent(String content) {
        this.content = content;
    }
}
