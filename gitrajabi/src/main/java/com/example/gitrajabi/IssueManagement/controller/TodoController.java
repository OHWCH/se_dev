package com.example.gitrajabi.IssueManagement.controller;

import com.example.gitrajabi.IssueManagement.dto.TodoRequestDto;
import com.example.gitrajabi.IssueManagement.dto.TodoResponseDto;
import com.example.gitrajabi.IssueManagement.service.TodoService;
import com.example.gitrajabi.IssueManagement.dto.TodoBatchDeleteRequestDto;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.web.PageableDefault;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/todos")
public class TodoController {

    private final TodoService todoService;
    private final Long devUserId = 1L; // 테스트용 고정 ID

    public TodoController(TodoService todoService) {
        this.todoService = todoService;
    }

    /**
     * 할 일 생성
     * POST /api/todos
     * Body: { "content": "알고리즘 문제 풀기" }
     */
    @PostMapping
    public TodoResponseDto createTodo(@RequestBody TodoRequestDto request) {
        return todoService.createTodo(devUserId, request);
    }

    /**
     * 할 일 목록 조회 (무한 스크롤)
     * GET /api/todos?page=0&size=5
     * 반환값: content(리스트), last(마지막 페이지 여부) 등 포함된 JSON
     */
    @GetMapping
    public Slice<TodoResponseDto> getTodos(
            @PageableDefault(size = 5) Pageable pageable // 기본 5개씩 조회
    ) {
        return todoService.getTodos(devUserId, pageable);
    }

    /**
     * 할 일 완료 체크/해제
     * PATCH /api/todos/{todoId}/check
     */
    @PatchMapping("/{todoId}/check")
    public void toggleTodo(@PathVariable Long todoId) {
        todoService.toggleTodo(todoId);
    }

    /**
     * [변경] 할 일 일괄 삭제 (Batch Delete)
     * POST /api/todos/batch-delete
     * Body: { "todoIds": [1, 3, 5] }
     */
    @PostMapping("/batch-delete")
    public void deleteTodos(@RequestBody TodoBatchDeleteRequestDto request) {
        todoService.deleteTodos(request.todoIds());
    }
}