import React from 'react';
import MaterialSymbol from '../ui/MaterialSymbol';
import { Link } from 'react-router-dom';

const PostListItem = ({ post }) => {
    return (
        <li className="hover:bg-gray-50 dark:hover:bg-gray-700/50 transition-colors duration-150">
            <Link to={`/community/post/${post.postId}`} className="block px-6 py-4">
                <div className="flex justify-between items-start gap-4">
                    <div className="flex-grow min-w-0">
                        <p className="font-semibold text-base truncate text-gray-900 dark:text-white">
                            {post.title}
                        </p>
                        <div className="flex items-center text-sm text-gray-500 dark:text-gray-400 mt-2">
                            {/* ✅ 작성자 표시 수정 */}
                            <span>{post.authorGithubId || '익명'}</span>
                            <span className="mx-2">·</span>
                            <span>{new Date(post.createdAt).toLocaleDateString('ko-KR')}</span>
                        </div>
                    </div>
                    <div className="flex-shrink-0 text-sm text-gray-500 dark:text-gray-400 font-medium pt-0.5">
                        <span className="flex items-center">
                            <MaterialSymbol name="chat_bubble" className="text-sm mr-1" style={{ fontSize: '1rem' }} />
                            {post.commentCount || 0}
                        </span>
                    </div>
                </div>
            </Link>
        </li>
    );
};

export default PostListItem;
