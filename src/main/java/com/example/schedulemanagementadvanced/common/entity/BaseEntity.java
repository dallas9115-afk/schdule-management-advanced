package com.example.schedulemanagementadvanced.common.entity;

import jakarta.persistence.Column;
import jakarta.persistence.EntityListeners;
import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Getter
@MappedSuperclass //  자식들에게 컬럼만 물려주는 부모 클래스
@EntityListeners(AuditingEntityListener.class) // Auditing를 붙여서 시간을 자동 추적
public abstract class BaseEntity {

    @CreatedDate
    @Column(updatable = false) // 생성일은 수정 제한
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
