import React, { useState, useEffect, useCallback } from 'react';
import { getTodoList, createTodo, toggleTodoCheck, batchDeleteTodos } from '../../services/userApi';
import MaterialSymbol from '../ui/MaterialSymbol';
const LoadingSpinner = () => <div className="text-center text-primary">로딩 중...</div>;


const TodoItem = ({ todo, handleToggleCheck, handleToggleSelect, isSelected, isToggling }) => (
    <div key={todo.id} className="flex items-center pt-3 first:pt-0 justify-between group">
        
        {/* 좌측: 삭제 선택 체크박스 + 내용 */}
        <div className="flex items-center flex-1 min-w-0">
            {/* 1. 삭제 선택 체크박스 (왼쪽) */}
            <input 
                className="h-5 w-5 rounded border-gray-300 text-red-500 focus:ring-red-500 dark:bg-gray-700 dark:border-gray-600 mr-3 flex-shrink-0" // flex-shrink-0 추가
                id={`select-${todo.id}`} 
                type="checkbox" 
                checked={isSelected} 
                onChange={() => handleToggleSelect(todo.id)}
            />

            {/* 2. 할 일 내용 (체크 상태에 따라 취소선 적용) */}
            <label 
                className={`flex-1 text-gray-700 dark:text-gray-300 truncate ${
                    todo.isChecked ? 'line-through text-gray-500 dark:text-gray-500' : ''
                }`} 
                htmlFor={`select-${todo.id}`}
            >
                {todo.content}
            </label>
        </div>
        
        {/* 우측: 완료 상태 토글 버튼 (아이콘 + 텍스트) */}
        <button
            onClick={() => handleToggleCheck(todo.id, todo.isChecked)}
            disabled={isToggling}
            className={`flex-shrink-0 ml-4 px-3 py-1 text-sm font-medium rounded-full transition-colors flex items-center ${
                isToggling 
                    ? 'opacity-50' 
                    : todo.isChecked 
                        ? 'bg-primary/10 text-primary dark:bg-primary/20 hover:bg-primary/20' // 완료된 상태 배경
                        : 'bg-gray-100 text-gray-600 dark:bg-gray-700 dark:text-gray-300 hover:bg-gray-200 dark:hover:bg-gray-600' // 미완료 상태 배경
            }`}
            aria-label={todo.isChecked ? "할 일 취소" : "할 일 완료"}
        >
            {/* 텍스트: 완료/취소 상태에 따라 표시 */}
            <span className="mr-1">
                {todo.isChecked ? '완료됨' : '완료'} 
            </span>

            {/* 아이콘: 완료 상태 표시 */}
            <MaterialSymbol 
                name={todo.isChecked ? "check_circle" : "circle"} 
                className={`text-lg ${
                    todo.isChecked ? 'text-primary' : 'text-gray-400 dark:text-gray-500'
                }`}
            />
        </button>
    </div>
);

const TodoList = () => {
    // 🌟 1. 상태 추가 및 초기화
    const [todos, setTodos] = useState([]); 
    const [selectedIds, setSelectedIds] = useState([]); // 선택 삭제용 ID 목록
    const [newTodoText, setNewTodoText] = useState(''); // 할 일 입력 필드
    const [loading, setLoading] = useState(true); // 초기/추가 로딩 상태
    const [isCreating, setIsCreating] = useState(false); // 생성 중 상태
    // 무한 스크롤을 위한 상태
    const [page, setPage] = useState(0); 
    const [isLast, setIsLast] = useState(false);
    const pageSize = 5; 

    // 🌟 2. 할 일 목록 조회 로직 (Fetch - 5. 조회)
    const fetchTodos = useCallback(async (pageNum) => {
        // 첫 페이지 로드이거나, 추가 로드가 가능할 때만 로딩 시작
        if (pageNum === 0) setLoading(true); 
        if (pageNum > 0 && isLast) return; 

        try {
            const data = await getTodoList(pageNum, pageSize); 
            
            // 페이지 번호 및 마지막 페이지 여부 업데이트
            setPage(data.number);
            setIsLast(data.last);

            if (pageNum === 0) {
                // 초기 로드: 목록 전체 교체
                setTodos(data.content || []); 
            } else {
                // 5. 스크롤 바닥: 기존 리스트 뒤에 이어 붙이기
                setTodos(prevTodos => [...prevTodos, ...(data.content || [])]);
            }
            
        } catch (error) {
            console.error("할 일 목록을 불러오는 데 실패했습니다:", error);
        } finally {
            if (pageNum === 0) setLoading(false);
        }
    }, [isLast]); // isLast가 변경될 때만 fetchTodos가 재생성되도록 설정

    // 초기 로드: 마이페이지 진입 시 page=0 호출
    useEffect(() => {
        fetchTodos(0);
    }, [fetchTodos]);

    // 🌟 3. 새 할 일 생성 핸들러 (4. 생성)
    const handleCreateNewTodo = async (e) => {
        if (e) e.preventDefault(); 
        
        const trimmedContent = newTodoText.trim();
        if (trimmedContent === '') return;

        setIsCreating(true);
        try {
            // 1. API 호출 성공 시 생성된 객체(newTodo) 응답
            const newTodo = await createTodo(trimmedContent); 
            
            // 🚨 요청사항 2, 3: 전체 목록 재조회 없이 응답받은 객체를 리스트 맨 앞에 추가 (unshift)
            setTodos(prevTodos => [newTodo, ...prevTodos]);
            
            setNewTodoText(''); // 입력 필드 초기화
        } catch (error) {
            alert("할 일 생성에 실패했습니다.");
        } finally {
            setIsCreating(false);
        }
    };

    // 선택 토글 핸들러 (삭제 선택용)
    const handleToggleSelect = (id) => {
        setSelectedIds(prevIds => 
            prevIds.includes(id) 
                ? prevIds.filter(itemId => itemId !== id) // 이미 있으면 제거
                : [...prevIds, id] // 없으면 추가
        );
    };

    // 🌟 4. 체크 토글 핸들러 (6. 체크 토글)
    const handleToggleCheck = async (todoId, isChecked) => {
        // 로컬 상태를 먼저 변경하여 즉각적인 UI 반응을 제공 (Optimistic Update)
        setTodos(prevTodos => 
            prevTodos.map(todo => 
                todo.id === todoId 
                    ? { ...todo, isChecked: !isChecked } // isChecked 상태 반전
                    : todo
            )
        );
        
        try {
            // API 호출 (PATCH)
            await toggleTodoCheck(todoId);
            
            // API 호출 실패 시 (선택 사항): 상태를 원래대로 되돌리는 로직 추가 가능
            // (여기서는 단순화를 위해 생략함)
        } catch (error) {
            alert("할 일 상태 변경에 실패했습니다. (API 오류)");
             // 실패 시 상태 롤백
             setTodos(prevTodos => 
                prevTodos.map(todo => 
                    todo.id === todoId 
                        ? { ...todo, isChecked: isChecked } // 원래 isChecked 상태로 복원
                        : todo
                )
            );
        }
    };
    
    // 🌟 5. 선택 삭제 핸들러 (7. 선택 삭제)
    const handleDeleteSelected = async () => {
        if (selectedIds.length === 0) return;

        if (!window.confirm(`${selectedIds.length}개의 할 일을 삭제하시겠습니까?`)) return;

        try {
            // 1. API 호출 (POST /batch-delete)
            await batchDeleteTodos(selectedIds);

            // 🚨 요청사항 2, 3: 전체 목록 재조회 없이 삭제된 ID들을 filter()로 제외하여 화면 갱신
            setTodos(prevTodos => prevTodos.filter(todo => !selectedIds.includes(todo.id)));
            
            setSelectedIds([]); // 선택된 항목 초기화
        } catch (error) {
            alert("할 일 삭제에 실패했습니다.");
        }
    };

    // 무한 스크롤 핸들러 (스크롤 바닥 감지 시)
    const handleScroll = (e) => {
        const { scrollTop, clientHeight, scrollHeight } = e.currentTarget;
        
        // 스크롤이 바닥에 닿았을 때 (예: 스크롤 높이의 90% 이상)
        if (scrollHeight - scrollTop < clientHeight + 50) { // 50px 여유 공간
            // 현재 로딩 중이 아니고, 마지막 페이지가 아닐 때만 다음 페이지 로드
            if (!loading && !isLast) {
                // 다음 페이지 호출 (page + 1)
                fetchTodos(page + 1);
            }
        }
    };

    if (loading && page === 0) {
        return <div className="p-6 h-48 flex justify-center items-center"><LoadingSpinner /></div>;
    }

    const noTodosMessage = todos.length === 0 && !loading && (
        <div className="py-4 text-center text-gray-500 dark:text-gray-400">
            새로운 할 일을 추가해보세요!
        </div>
    );
    
    return (
        <div className="lg:col-span-2 bg-white dark:bg-gray-800 p-6 rounded-lg shadow-md border border-gray-200 dark:border-gray-700">
            <div className="flex justify-between items-center mb-4">
                <h3 className="text-xl font-bold text-gray-900 dark:text-white">오늘의 할 일</h3>
                {/* 삭제 버튼 */}
                <button 
                    onClick={handleDeleteSelected}
                    disabled={selectedIds.length === 0}
                    className={`px-3 py-1 text-sm font-medium rounded-full transition-colors flex items-center ${
                        selectedIds.length > 0 
                            ? 'bg-red-500 text-white hover:bg-red-600'
                            : 'bg-gray-300 text-gray-500 cursor-not-allowed dark:bg-gray-700 dark:text-gray-400'
                    }`}
                >
                    <MaterialSymbol name="delete" className="text-lg" />
                    <span className="ml-1">삭제 ({selectedIds.length})</span>
                </button>
            </div>

            {/* 할 일 추가 폼 */}
            <form onSubmit={handleCreateNewTodo} className="mb-6 flex space-x-2">
                <input
                    type="text"
                    value={newTodoText}
                    onChange={(e) => setNewTodoText(e.target.value)}
                    placeholder="새로운 할 일을 입력하세요"
                    className="flex-1 px-4 py-2 border border-gray-300 rounded-lg focus:ring-primary focus:border-primary dark:bg-gray-700 dark:border-gray-600 dark:text-white"
                    disabled={isCreating}
                />
                <button
                    type="submit"
                    className="flex-shrink-0 px-4 py-2 bg-primary text-white font-medium rounded-lg hover:bg-indigo-700 transition-colors disabled:opacity-50 flex items-center justify-center"
                    disabled={isCreating || newTodoText.trim() === ''}
                >
                    {isCreating ? <LoadingSpinner size="small" /> : '추가'}
                </button>
            </form>

            {/* 할 일 목록 (스크롤 가능 영역) */}
            <div 
                className="space-y-3 divide-y divide-gray-200 dark:divide-gray-700 overflow-y-auto max-h-96" 
                onScroll={handleScroll} // 🌟 스크롤 이벤트 핸들러 연결
            >
                {todos.length > 0 ? (
                    todos.map((todo) => (
                        <TodoItem 
                            key={todo.id} 
                            todo={todo} 
                            handleToggleCheck={handleToggleCheck} 
                            handleToggleSelect={handleToggleSelect}
                            isSelected={selectedIds.includes(todo.id)}
                        />
                    ))
                ) : noTodosMessage}
                
                {/* 🌟 추가 로딩 중 표시 (무한 스크롤) */}
                {loading && page > 0 && (
                    <div className="py-2 text-center text-gray-500 dark:text-gray-400">
                        <LoadingSpinner size="small" />
                    </div>
                )}

                {/* 마지막 페이지 표시 */}
                {isLast && todos.length > pageSize && (
                    <div className="py-2 text-center text-gray-500 dark:text-gray-400 text-sm">
                        --- 목록의 끝입니다 ---
                    </div>
                )}
            </div>
        </div>
    );
};

export default TodoList;