import React from 'react';
import Header from '../components/ui/Header';
import PostList from '../components/community/PostList';
import Pagination from '../components/ui/Pagination';
import { mockPosts, mockPaginationLinks } from '../data/communityData'; // 데이터 import

const CommunityPage = () => {
    return (
        // 전역 스타일링 (Homepage와 동일)
        <div className="min-h-screen bg-background-light dark:bg-background-dark font-display text-gray-800 dark:text-gray-200 antialiased">
            <Header activeLink="community" /> {/* 🌟 activeLink prop 전달 */}
            
            <main className="container mx-auto px-4 sm:px-6 lg:px-8 py-8">
                {/* PostList 컴포넌트가 max-w-4xl을 내부적으로 처리 */}
                <PostList posts={mockPosts} />
                
                {/* 페이지네이션 (Homepage 재사용) */}
                <div className="max-w-4xl mx-auto">
                    <Pagination links={mockPaginationLinks} />
                </div>
            </main>
        </div>
    );
};

export default CommunityPage;