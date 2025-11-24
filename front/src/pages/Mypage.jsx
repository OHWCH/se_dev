import React from 'react';
import Header from '../components/ui/Header';
import ProfileSummary from '../components/profile/ProfileSummary';
import TodoList from '../components/profile/TodoList';
import StudyList from '../components/profile/StudyList';

const MyPage = () => {
    
    return (
        <div className="min-h-screen bg-background-light dark:bg-background-dark font-display text-gray-800 dark:text-gray-200 antialiased">
            <Header />
            
            {/* 메인컨텐츠 최대 너비 7xl, 중앙 정렬 */}
            <main className="max-w-7xl mx-auto py-8 px-4 sm:px-6 lg:px-8">
                <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
                    {/* 프로필 요약 */}
                    <ProfileSummary />
                    
                    {/* To-Do 리스트 */}
                    <TodoList />
                </div>
                
                {/* 스터디 목록 */}
                <StudyList />
            </main>
        </div>
    );
};

export default MyPage;