import React, { useState, useEffect } from 'react';
import { getTodoList, createTodo } from '../../services/userApi';
import MaterialSymbol from '../ui/MaterialSymbol';

const TodoList = () => {

    const [todos, setTodos] = useState([]); 
    const [selectedIds, setSelectedIds] = useState([]);
    const [newTodoText, setNewTodoText] = useState('');
    const [loading, setLoading] = useState(true); // 🌟 로딩 상태 추가
    const [isCreating, setIsCreating] = useState(false);


    useEffect(() => {
        const fetchTodos = async () => {
            setLoading(true); // 로딩 시작
            try {
                // 🚨 API 호출
                const data = getTodoList(); 
                
                // API 응답 데이터가 할 일 목록 배열이라고 가정
                // data가 { todos: [...] } 형태일 경우 data.todos를 사용하도록 조정 필요
                setTodos(data || []); 
                
            } catch (error) {
                console.error("할 일 목록을 불러오는 데 실패했습니다:", error);
                // 실패 시에도 빈 배열로 유지
                setTodos([]); 
            } finally {
                setLoading(false); // 로딩 종료
            }
        };

        fetchTodos();
    }, []);

    const handleToggleSelect = (id) => {
        setSelectedIds(prevIds => 
            prevIds.includes(id) 
                ? prevIds.filter(itemId => itemId !== id) // 이미 있으면 제거
                : [...prevIds, id] // 없으면 추가
        );
    };

    const handleDeleteSelected = () => {
        if (selectedIds.length === 0) return;

        // 선택되지 않은 항목들만 필터링하여 새로운 todos 배열 생성
        const newTodos = todos.filter(todo => !selectedIds.includes(todo.id));
        
        // 🚨 API를 통해 삭제하는 로직이 추가되어야 하지만, 여기서는 클라이언트 상태만 업데이트합니다.
        // (ex: deleteTodoApi(selectedIds) 호출 필요)
        
        setTodos(newTodos); // 상태 업데이트
        setSelectedIds([]); // 선택된 항목 초기화
    };

    const handleCreateNewTodo = async (e) => {
        if (e) e.preventDefault(); 
        
        const trimmedText = newTodoText.trim();
        
        if (trimmedText === '') {
            return; 
        }

        // 🌟 생성 요청 중 중복 실행 방지
        if (isCreating) return; 

        setIsCreating(true); // 로딩 시작 (버튼 비활성화 목적)

        try {
            // 🚨 1. API 호출 (할 일 내용만 전송)
            const newTodoFromApi = await createTodo(trimmedText); 

            // 🚨 2. 응답받은 객체를 리스트의 맨 앞에 추가 (unshift 효과)
            // 서버 응답 형태가 { id: 1, content: "...", isChecked: false } 라고 가정
            // 프론트엔드에서 사용하는 필드명에 맞게 변환합니다.
            const formattedNewTodo = {
                id: newTodoFromApi.id,
                text: newTodoFromApi.content, // content를 text로 매핑
                completed: newTodoFromApi.isChecked, // isChecked를 completed로 매핑
            };
            
            // setTodos를 사용하여 배열 맨 앞에 추가
            setTodos(prevTodos => [formattedNewTodo, ...prevTodos]); 
            
            // 3. 입력 필드 초기화
            setNewTodoText('');

        } catch (error) {
            // API 호출 실패 시 에러 알림
            alert(`할 일 생성 실패: ${error.message}`);
        } finally {
            setIsCreating(false); // 로딩 종료
        }
    };

    if (loading) {
        return (
            <div className="bg-white dark:bg-gray-800 p-6 rounded-lg shadow-xl border border-gray-200 dark:border-gray-700">
                <p className="text-center text-gray-500 dark:text-gray-400">할 일 목록을 불러오는 중...</p>
            </div>
        );
    }

    return (
        <div className="lg:col-span-2 bg-white dark:bg-gray-800 p-6 rounded-lg shadow-md border border-gray-200 dark:border-gray-700">
            <div className="flex justify-between items-center mb-4">
                <h3 className="text-lg font-semibold text-gray-900 dark:text-white">To-Do List</h3>
                
                {/* 🌟 삭제 버튼 추가 */}
                <button
                    onClick={handleDeleteSelected}
                    disabled={selectedIds.length === 0}
                    className={`flex items-center text-sm font-semibold px-3 py-1.5 rounded-md transition-colors 
                        ${selectedIds.length > 0 
                            ? 'bg-red-500 text-white hover:bg-red-600'
                            : 'bg-gray-200 text-gray-500 cursor-not-allowed dark:bg-gray-700 dark:text-gray-400'
                        }`}
                >
                    <MaterialSymbol name="delete" className="mr-1 text-base" />
                    삭제 ({selectedIds.length})
                </button>
            </div>

            <form onSubmit={handleCreateNewTodo} className="flex space-x-2">
                <input
                    type="text"
                    className="flex-grow rounded-md border-gray-300 shadow-sm focus:border-primary focus:ring-primary dark:bg-gray-700 dark:border-gray-600 dark:text-white"
                    placeholder="새로운 할 일을 입력하세요"
                    value={newTodoText}
                    onChange={(e) => setNewTodoText(e.target.value)}
                    // 🌟 생성 중일 때는 입력 비활성화
                    disabled={isCreating} 
                />
                <button
                    type="submit"
                    className="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-primary hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary transition-colors"
                    // 🌟 생성 중일 때는 버튼 비활성화
                    disabled={isCreating}
                >
                    추가
                    {isCreating && <MaterialSymbol name="progress_activity" className="text-lg ml-1 animate-spin" />}
                </button>
            </form>
            
            <div className="space-y-3 divide-y divide-gray-200 dark:divide-gray-700">
                {todos.length > 0 ? (
                    // 🌟 todos 상태를 맵핑하도록 변경
                    todos.map((todo) => (
                        <div key={todo.id} className="flex items-center pt-3 first:pt-0">
                            <input 
                                className="h-5 w-5 rounded border-gray-300 text-primary focus:ring-primary dark:bg-gray-700 dark:border-gray-600" 
                                id={`todo-${todo.id}`} 
                                type="checkbox" 
                                checked={selectedIds.includes(todo.id)} 
                                onChange={() => handleToggleSelect(todo.id)}
                            />
                            <label 
                                className={`ml-3 text-gray-700 dark:text-gray-300 ${selectedIds.includes(todo.id) ? 'line-through text-gray-500 dark:text-gray-500' : ''}`} 
                                htmlFor={`todo-${todo.id}`}
                            >
                                {todo.text}
                            </label>
                        </div>
                    ))
                ) : (
                    <div className="py-4 text-center text-gray-500 dark:text-gray-400">
                        할 일이 모두 완료되었습니다!
                    </div>
                )}
            </div>
        </div>
    );
};

export default TodoList;