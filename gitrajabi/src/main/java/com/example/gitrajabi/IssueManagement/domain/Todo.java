package com.example.gitrajabi.IssueManagement.domain;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;

@Entity
@Getter
@Table(name = "todo")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(AuditingEntityListener.class) // 생성 시간 자동 관리
public class Todo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private Long userId; // 유저 매핑

    @Column(nullable = false)
    private String content; // 할 일 내용

    private boolean isChecked; // 완료 여부

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt; // 생성 시간 (정렬용)

    @Builder
    public Todo(Long userId, String content) {
        this.userId = userId;
        this.content = content;
        this.isChecked = false; // 기본값은 미완료
    }

    // 체크 상태 변경 메소드 (Dirty Checking)
    public void toggle() {
        this.isChecked = !this.isChecked;
    }
}