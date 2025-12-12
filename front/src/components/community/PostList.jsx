import React from 'react';
import PostListItem from './PostListItem';
import MaterialSymbol from '../ui/MaterialSymbol';

const PostList = ({ posts }) => {
    return (
        <div className="max-w-4xl mx-auto">
            {/* 상단 제목 및 글 작성 버튼 영역 */}
            <div className="flex justify-between items-center mb-8">
                <h2 className="text-2xl sm:text-3xl font-bold tracking-tight text-gray-900 dark:text-white">자유게시판</h2>
                <a className="flex items-center justify-center bg-primary text-white font-medium px-4 py-2 rounded-md text-sm hover:opacity-90 transition-opacity" href="/postwrite">
                    <MaterialSymbol name="edit" className="text-sm mr-2" />
                    글 작성
                </a>
            </div>

            {/* 게시물 목록 컨테이너 */}
            <div className="bg-white dark:bg-gray-800 rounded-lg shadow-sm border border-gray-200 dark:border-gray-700 overflow-hidden">
                <ul className="divide-y divide-gray-200 dark:divide-gray-700" role="list">
                    {posts.map((post) => (
                        <PostListItem key={post.postId} post={post} />
                    ))}
                </ul>
            </div>
        </div>
    );
};

export default PostList;