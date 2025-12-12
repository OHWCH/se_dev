package com.example.gitrajabi.IssueManagement.dto;

import java.util.List;

public record TodoBatchDeleteRequestDto(
        List<Long> todoIds // 삭제할 할 일의 ID 목록 (예: [1, 3, 5])
) {}