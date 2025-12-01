import React from 'react';
import MaterialSymbol from './MaterialSymbol';

// 🌟 Props 추가: keyword, onKeywordChange, onSearch
const SearchBar = ({ keyword, onKeywordChange, onSearch }) => {

    // 폼 제출 핸들러 (Enter 키 입력 시 onSearch 호출)
    const handleFormSubmit = (e) => {
        e.preventDefault(); // 기본 새로고침 방지
        onSearch(); // 검색 실행
    };

    return (
        // 🌟 <form> 태그로 감싸고 onSubmit 핸들러 연결
        <form onSubmit={handleFormSubmit} className="flex flex-col sm:flex-row items-center justify-between gap-4 mb-8">
            <div className="relative w-full"> 
                <MaterialSymbol name="search" className="absolute left-3 top-1/2 -translate-y-1/2 text-gray-400 dark:text-gray-500" />
                <input 
                    className="w-full pl-10 pr-4 py-2 border border-gray-300 dark:border-gray-600 bg-white dark:bg-gray-700 rounded-md focus:ring-primary focus:border-primary text-gray-900 dark:text-gray-100 placeholder-gray-400 dark:placeholder-gray-500" 
                    placeholder="Search for good first issues..." 
                    type="search"
                    value={keyword} // 🌟 keyword 상태 연결
                    onChange={onKeywordChange} // 🌟 변경 핸들러 연결
                />
            </div>
            
            {/* 🌟 검색 버튼 추가 */}
            <button 
                type="submit" // 폼 제출 역할
                onClick={onSearch} // 버튼 클릭 시 onSearch 호출
                className="w-full sm:w-auto flex-shrink-0 px-4 py-2 bg-primary text-white font-semibold rounded-md hover:bg-indigo-700 transition-colors"
            >
                검색
            </button>
        </form>
    );
};

export default SearchBar;