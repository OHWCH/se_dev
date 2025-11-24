import React from 'react';
import Header from '../components/ui/Header';
import MaterialSymbol from '../components/ui/MaterialSymbol';

const PostWritePage = () => {
    return (
        <div className="flex flex-col min-h-screen bg-background-light dark:bg-background-dark font-display text-text-light-primary dark:text-text-dark-primary antialiased">
            <Header /> 
            
            <main className="flex-grow container mx-auto px-4 sm:px-6 lg:px-8 py-8">
                <div className="max-w-4xl mx-auto">
                    <div className="flex justify-between items-center mb-6">
                        <h1 className="text-2xl font-bold text-text-light-primary dark:text-text-dark-primary">게시글 작성</h1>
                        <button className="bg-primary text-white px-6 py-2 rounded-md text-sm font-semibold shadow-sm hover:opacity-90 transition-colors">
                            저장
                        </button>
                    </div>
                    
                    {/* 폼 컨테이너 */}
                    <div className="bg-surface-light dark:bg-surface-dark rounded-lg shadow-sm border border-border-light dark:border-border-dark">
                        <div className="p-6 sm:p-8 space-y-6">
                            <div>
                                <label className="sr-only" htmlFor="title">제목</label>
                                {/* 제목 입력 필드 */}
                                <input 
                                    className="w-full bg-transparent border-0 border-b border-border-light dark:border-border-dark p-0 text-xl font-semibold text-text-light-primary dark:text-text-dark-primary placeholder-text-light-secondary dark:placeholder-text-dark-secondary focus:ring-0 focus:border-primary" 
                                    id="title" 
                                    name="title" 
                                    placeholder="제목을 입력하세요." 
                                    type="text"
                                />
                            </div>
                            <div>
                                <label className="sr-only" htmlFor="content">내용</label>
                                {/* 내용 입력 필드 */}
                                <textarea 
                                    className="w-full bg-transparent border-0 p-0 text-base text-text-light-primary dark:text-text-dark-secondary placeholder-text-light-secondary dark:placeholder-text-dark-secondary focus:ring-0 resize-none" 
                                    id="content" 
                                    name="content" 
                                    placeholder="내용을 입력하세요." 
                                    rows="15"
                                />
                            </div>
                        </div>
                    </div>
                </div>
            </main>
        </div>
    );
};

export default PostWritePage;