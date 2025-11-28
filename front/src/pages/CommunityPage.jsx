import React, { useState, useEffect } from 'react';
import Header from '../components/ui/Header';
import PostList from '../components/community/PostList';
import Pagination from '../components/ui/Pagination';
import { getPostList } from '../services/postApi';
import { mockPaginationLinks } from '../data/communityData'; // 데이터 import

const CommunityPage = () => {

    const [ posts, setPosts ] = useState([]);
    const [loading, setLoading] = useState(true); // 로딩 상태
    const [error, setError] = useState(null); // 에러 메시지

    useEffect(() => {
            const fetchPosts = async () => {
                setLoading(true); // 로딩 시작
                try {
                    const fetchedPosts = await getPostList(); 
                    
                    setPosts(fetchedPosts); 
                    
                    setError(null);
                } catch (err) {
                    console.error("데이터 패칭 오류:", err);
                    setError("게시글 목록을 불러오는 데 실패했습니다."); 
                    setPosts([]);
                } finally {
                    setLoading(false); // 로딩 종료
                }
            };
            
            fetchPosts();
        }, []); // 훅이 마운트될 때 한 번만 실행


    return (
        // 전역 스타일링 (Homepage와 동일)
        <div className="min-h-screen bg-background-light dark:bg-background-dark font-display text-gray-800 dark:text-gray-200 antialiased">
            <Header activeLink="community" /> {/* 🌟 activeLink prop 전달 */}
            
            <main className="container mx-auto px-4 sm:px-6 lg:px-8 py-8">
                {/* PostList 컴포넌트가 max-w-4xl을 내부적으로 처리 */}
                <PostList posts={posts} />
                
                {/* 페이지네이션 (Homepage 재사용) */}
                <div className="max-w-4xl mx-auto">
                    <Pagination links={mockPaginationLinks} />
                </div>
            </main>
        </div>
    );
};

export default CommunityPage;