import React from 'react';
import MaterialSymbol from '../ui/MaterialSymbol';
import { Link } from 'react-router-dom'; // 게시물 상세 페이지로 이동 가정

const PostListItem = ({ post }) => {
    return (
        <li className="hover:bg-gray-50 dark:hover:bg-gray-700/50 transition-colors duration-150">
            {/* Link 컴포넌트 대신 a 태그로 구현하여 시안과 동일하게 href 속성 사용 */}
            <Link to={post.href} className="block px-6 py-4">
                <div className="flex justify-between items-start gap-4">
                    <div className="flex-grow min-w-0">
                        <p className="font-semibold text-base truncate text-gray-900 dark:text-white">{post.title}</p>
                        <div className="flex items-center text-sm text-gray-500 dark:text-gray-400 mt-2">
                            <span>{post.author}</span>
                            <span className="mx-2">·</span>
                            <span>{post.date}</span>
                        </div>
                    </div>
                    {/* 댓글 수 표시 */}
                    <div className="flex-shrink-0 text-sm text-gray-500 dark:text-gray-400 font-medium pt-0.5">
                        <span className="flex items-center">
                            <MaterialSymbol name="chat_bubble" className="text-sm mr-1" style={{ fontSize: '1rem' }} />
                            {post.comments}
                        </span>
                    </div>
                </div>
            </Link>
        </li>
    );
};

export default PostListItem;