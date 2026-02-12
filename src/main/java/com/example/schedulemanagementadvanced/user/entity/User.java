package com.example.schedulemanagementadvanced.user.entity;

import com.example.schedulemanagementadvanced.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Table(name = "users") // SQL 예약어 'user' 와 충돌 방지를 위해 'users' 로 명명
@NoArgsConstructor // JPA는 기본 생성자 (보통 protected로 막음)
public class User extends BaseEntity {

    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id; // PK

    @Column(nullable = false, length = 10) // DB 제약조건: null 불가, 10자 제한
    private String username;

    @Column(nullable = false, unique = true) // 이메일은 중복되면 안 됨 (Unique Key)
    private String email;

    @Column(nullable = false)
    private String password;

    // 생성자: 필수 데이터는 생성 시점에 강제 주입
    public User(String username, String email, String password) {
        this.username = username;
        this.email = email;
        this.password = password;
    }
}
