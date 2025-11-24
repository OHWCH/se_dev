import React, { useState } from 'react';
import { todoList } from '../../data/mypagedata'; // Mock Data import
import MaterialSymbol from '../ui/MaterialSymbol';

const TodoList = () => {

    const [todos, setTodos] = useState(todoList); 
    const [selectedIds, setSelectedIds] = useState([]);
    const [newTodoText, setNewTodoText] = useState('');

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
        
        setTodos(newTodos); // 상태 업데이트
        setSelectedIds([]); // 선택된 항목 초기화
    };

    const handleCreateNewTodo = (e) => {
        // 폼 제출 시 새로고침 방지 (엔터 키 입력 시에도 작동하도록)
        if (e) e.preventDefault(); 
        
        const trimmedText = newTodoText.trim();
        
        if (trimmedText === '') {
            return; // 입력값이 비어있으면 종료
        }

        const newTodo = {
            // 현재 시간을 사용하여 고유 ID 생성 (백엔드 연결 시 실제 ID 사용)
            id: Date.now(), 
            text: trimmedText,
            completed: false,
        };

        // 새로운 항목을 리스트 맨 앞에 추가
        setTodos([newTodo, ...todos]); 
        setNewTodoText(''); // 입력 필드 초기화
    };

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

            <form onSubmit={handleCreateNewTodo} className="flex space-x-2 mb-6">
                <input
                    type="text"
                    placeholder="새로운 할 일 입력..."
                    value={newTodoText} // 🌟 상태와 연결
                    onChange={(e) => setNewTodoText(e.target.value)} // 🌟 입력 값 업데이트
                    className="flex-grow px-4 py-2 border border-gray-300 dark:border-gray-600 rounded-lg bg-white dark:bg-gray-700 text-gray-900 dark:text-white focus:ring-primary focus:border-primary text-sm"
                />
                <button
                    type="submit" // form의 submit 이벤트 발생
                    className="flex items-center justify-center bg-primary text-white font-medium px-4 py-2 rounded-lg text-sm hover:opacity-90 transition-opacity whitespace-nowrap"
                >
                    <MaterialSymbol name="add" className="text-lg" />
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
                                // 🌟 상태에 따라 체크 여부 연결
                                checked={selectedIds.includes(todo.id)} 
                                // 🌟 체크박스 변경 핸들러 연결
                                onChange={() => handleToggleSelect(todo.id)}
                            />
                            <label 
                                // 🌟 선택된 항목에만 취소선/색상 적용
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