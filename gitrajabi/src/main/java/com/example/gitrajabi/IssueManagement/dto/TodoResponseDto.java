package com.example.gitrajabi.IssueManagement.dto;

import com.example.gitrajabi.IssueManagement.domain.Todo;

public record TodoResponseDto(
        Long id,
        String content,
        boolean isChecked
) {
    // Entity -> DTO 변환 편의 메소드
    public static TodoResponseDto from(Todo todo) {
        return new TodoResponseDto(
                todo.getId(),
                todo.getContent(),
                todo.isChecked()
        );
    }
}