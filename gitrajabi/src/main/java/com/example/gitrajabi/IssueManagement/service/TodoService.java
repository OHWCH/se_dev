package com.example.gitrajabi.IssueManagement.service;

import com.example.gitrajabi.IssueManagement.domain.Todo;
import com.example.gitrajabi.IssueManagement.dto.TodoRequestDto;
import com.example.gitrajabi.IssueManagement.dto.TodoResponseDto;
import com.example.gitrajabi.IssueManagement.repository.TodoRepository;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

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

    // 3. 체크 토글 (보안 강화)
    public void toggleTodo(Long userId, Long todoId) {
        Todo todo = todoRepository.findById(todoId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 할 일입니다."));
        todo.toggle();

        // 소유자 확인
        if (!todo.getUserId().equals(userId)){
            throw new IllegalArgumentException("해당 할 일을 수정할 권한이 없습니다.");
        }

        todo.toggle();
    }

    /**
     * 4. 일괄 삭제 (보안 강화)
     * - 요청된 ID들 중, 실제 내 소유인 것만 골라서 삭제합니다.
     */
    public void deleteTodos(Long userId, List<Long> todoIds) {
        // 1. 요청된 ID들에 해당하는 투두조회
        List<Todo> todos = todoRepository.findAllById(todoIds);

        // 2. 내 소유인 것만 필터링
        List<Todo> myTodos = todos.stream()
                .filter(todo -> todo.getUserId().equals(userId))
                .collect(Collectors.toList());

        // 3. 삭제 수행
        if (!myTodos.isEmpty()) {
            todoRepository.deleteAll(myTodos);
        }
    }
}