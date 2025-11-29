package com.example.gitrajabi.IssueManagement.service;

import com.example.gitrajabi.IssueManagement.domain.Todo;
import com.example.gitrajabi.IssueManagement.dto.TodoRequestDto;
import com.example.gitrajabi.IssueManagement.dto.TodoResponseDto;
import com.example.gitrajabi.IssueManagement.repository.TodoRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class TodoService {

    private final TodoRepository todoRepository;

    public TodoService(TodoRepository todoRepository) {
        this.todoRepository = todoRepository;
    }

    // 1. 생성
    public TodoResponseDto createTodo(Long userId, TodoRequestDto request) {
        Todo todo = Todo.builder()
                .userId(userId)
                .content(request.content())
                .build();
        Todo savedTodo = todoRepository.save(todo);
        return TodoResponseDto.from(savedTodo);
    }

    // 2. 조회 (무한 스크롤)
    @Transactional(readOnly = true)
    public Slice<TodoResponseDto> getTodos(Long userId, Pageable pageable) {
        // Entity List -> DTO Slice 변환
        return todoRepository.findByUserIdOrderByCreatedAtDesc(userId, pageable)
                .map(TodoResponseDto::from);
    }

    // 3. 체크 토글 (완료/미완료)
    public void toggleTodo(Long todoId) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 할 일입니다."));
        todo.toggle();
        // @Transactional 덕분에 save 호출 없어도 자동 업데이트됨
    }

    // 4. 삭제
    public void deleteTodo(Long todoId) {
        todoRepository.deleteById(todoId);
    }
}