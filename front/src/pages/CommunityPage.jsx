// src/pages/CommunityPage.jsx
import React, { useState, useEffect } from 'react';
import Header from '../components/ui/Header';
import PostList from '../components/community/PostList';
import Pagination from '../components/ui/Pagination';
import { getPostList } from '../services/boardApi';  
import { showToast } from '../utils/toast';

const CommunityPage = () => {
  const [posts, setPosts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [error, setError] = useState(null);
  const [currentPage, setCurrentPage] = useState(0); // 페이지 상태 추가

  useEffect(() => {
    const fetchPosts = async () => {
      setLoading(true);
      try {
        // 백엔드 실제 API 호출 (page와 type 전달)
        const data = await getPostList(currentPage, "createdAt");
        
        // 백엔드 List<PostResponse>  그대로 사용
        setPosts(data || []);
        setError(null);
      } catch (err) {
        console.error("게시글 목록 불러오기 실패:", err);
        showToast.error("게시글을 불러오지 못했습니다");
        setError("게시글 목록을 불러오는 데 실패했습니다.");
        setPosts([]);
      } finally {
        setLoading(false);
      }
    };

    fetchPosts();
  }, [currentPage]); // 페이지 바뀔 때마다 다시 불러오기

  // 페이지 변경 핸들러
  const handlePageChange = (page) => {
    setCurrentPage(page);
    window.scrollTo(0, 0); // 페이지 위로 스크롤
  };

  return (
    <div className="min-h-screen bg-background-light dark:bg-background-dark font-display text-gray-800 dark:text-gray-200 antialiased">
      <Header activeLink="community" />
      
      <main className="container mx-auto px-4 sm:px-6 lg:px-8 py-8">
        {loading && (
          <div className="text-center py-12">
            <p className="text-lg">게시글을 불러오는 중...</p>
          </div>
        )}

        {error && (
          <div className="text-center py-12 text-red-500">
            <p>{error}</p>
          </div>
        )}

        {!loading && !error && (
          <>
            <PostList posts={posts} />
            
            <div className="max-w-4xl mx-auto mt-8">
              <Pagination 
                currentPage={currentPage}
                totalPages={10} 
                onPageChange={handlePageChange}
              />
            </div>
          </>
        )}
      </main>
    </div>
  );
};

export default CommunityPage;