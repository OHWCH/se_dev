import React from 'react';
import { Link } from 'react-router-dom';
import Header from '../components/ui/Header'; // 헤더 컴포넌트 (가정)
import MaterialSymbol from '../components/ui/MaterialSymbol'; // Material Symbol 컴포넌트 (가정)

// 시안에 사용된 데이터를 기반으로 Mock 데이터 정의
const mockPost = {
    id: 1,
    title: "제목 (Post Title)",
    author: "DevUser123",
    views: 1204,
    createdAt: "2023-10-27",
    content: `내용 (Content) - This is the main body of the post, formatted with markdown. It can contain paragraphs, lists, and other text formatting to provide a rich reading experience for developers and other users of the application.

            Code blocks are an essential part of a developer-focused platform. They should be clearly distinguishable and support syntax highlighting.

            \`\`\`javascript
            function helloWorld() {
            console.log("Hello, Git-ra-jab-i!");
            }
            helloWorld();
            \`\`\`

            The layout is designed to be clean and readable, with ample white space to prevent visual clutter. This ensures that the focus remains on the content itself.

            * List item one.
            * List item two.
            * List item three with a bit more text.

            End of post content.`,
};

const mockComments = [
    { id: 1, author: "Commenter1", date: "2023-10-27", text: "This is a great post! Very helpful information, thank you for sharing." },
    { id: 2, author: "AnotherDev", date: "2023-10-28", text: "I had a question about the code block. Is there a reason you chose that specific method over another one?" },
    { id: 3, author: "CodeNinja", date: "2023-10-29", text: "Thanks for the write-up. Looking forward to more content like this." },
];


const PostDetailPage = () => {
    // 실제 사용 시에는 URL 파라미터(id)를 사용하여 API에서 데이터를 fetch해야 합니다.
    const post = mockPost;
    const comments = mockComments;

    // 시안의 스타일을 그대로 유지하기 위해 Link 컴포넌트 사용 (Tailwind CSS 포함)
    return (
        <div className="relative flex h-auto min-h-screen w-full flex-col group/design-root overflow-x-hidden bg-background-light dark:bg-background-dark font-display text-text-light-primary dark:text-text-dark-primary antialiased">
            {/* Header 컴포넌트 */}
            <Header /> 
            
            <main className="layout-container flex h-full grow flex-col">
                <div className="container mx-auto px-4 flex flex-1 justify-center py-8">
                    <div className="layout-content-container flex flex-col max-w-4xl w-full flex-1 gap-6">
                        
                        {/* Breadcrumbs (경로) */}
                        <div className="flex flex-wrap gap-2 items-center text-sm">
                            <Link className="text-primary hover:underline font-medium leading-normal" to="/">Home</Link>
                            <span className="text-slate-400 dark:text-slate-500">/</span>
                            <Link className="text-primary hover:underline font-medium leading-normal" to="/community">Post List</Link>
                            <span className="text-slate-400 dark:text-slate-500">/</span>
                            <span className="text-slate-800 dark:text-slate-300 font-medium leading-normal">Post Detail</span>
                        </div>
                        
                        {/* Post Content Card */}
                        <div className="bg-surface-light dark:bg-surface-dark p-6 sm:p-8 rounded-xl border border-border-light dark:border-slate-800 shadow-sm">
                            <div className="flex flex-col sm:flex-row sm:items-start sm:justify-between gap-4">
                                <div className="flex-1">
                                    {/* 제목 */}
                                    <h1 className="text-slate-900 dark:text-white tracking-tight text-3xl font-bold leading-tight">{post.title}</h1>
                                    {/* 메타데이터 */}
                                    <p className="text-slate-500 dark:text-slate-400 text-sm font-normal leading-normal pt-2">
                                        작성자: {post.author} · 조회수: {post.views.toLocaleString()} · 생성일: {post.createdAt}
                                    </p>
                                </div>
                                
                                {/* 수정/삭제 버튼 */}
                                <div className="flex flex-shrink-0 gap-2">
                                    <button className="flex min-w-[84px] max-w-[480px] cursor-pointer items-center justify-center overflow-hidden rounded-lg h-10 px-4 bg-primary/20 dark:bg-primary/30 text-primary text-sm font-bold leading-normal tracking-[0.015em] hover:bg-primary/30 dark:hover:bg-primary/40 transition-colors">
                                        <span className="truncate">수정</span>
                                    </button>
                                    <button className="flex min-w-[84px] max-w-[480px] cursor-pointer items-center justify-center overflow-hidden rounded-lg h-10 px-4 bg-transparent text-red-500 dark:text-red-400 hover:bg-red-500/10 text-sm font-bold leading-normal tracking-[0.015em] transition-colors">
                                        <span className="truncate">삭제</span>
                                    </button>
                                </div>
                            </div>
                            
                            <hr className="my-6 border-border-light dark:border-slate-800"/>
                            
                            {/* 게시글 본문 (Markdown 처리 시 'prose' 클래스 사용) */}
                            {/* 시안의 HTML 구조를 따르기 위해 임시로 div와 pre를 사용합니다. */}
                            <article className="prose prose-slate dark:prose-invert max-w-none text-slate-700 dark:text-slate-300 leading-relaxed">
                                {/* 실제 애플리케이션에서는 post.content를 Markdown Renderer로 변환해야 합니다. */}
                                <p>내용 (Content) - This is the main body of the post, formatted with markdown. It can contain paragraphs, lists, and other text formatting to provide a rich reading experience for developers and other users of the application.</p>
                                <p>Code blocks are an essential part of a developer-focused platform. They should be clearly distinguishable and support syntax highlighting.</p>
                                <pre><code className="language-javascript">function helloWorld() {'\n'}  console.log("Hello, Git-ra-jab-i!");{'\n'}{'\n'}helloWorld();</code></pre>
                                <p>The layout is designed to be clean and readable, with ample white space to prevent visual clutter. This ensures that the focus remains on the content itself.</p>
                                <ul>
                                    <li>List item one.</li>
                                    <li>List item two.</li>
                                    <li>List item three with a bit more text.</li>
                                </ul>
                                <p>End of post content.</p>
                            </article>
                        </div>
                        
                        {/* Comment Area Card */}
                        <div className="bg-surface-light dark:bg-surface-dark p-6 sm:p-8 rounded-xl border border-border-light dark:border-slate-800 shadow-sm">
                            <h2 className="text-xl font-bold text-slate-900 dark:text-white mb-6">댓글 목록 (Comment List)</h2>
                            <div className="flex flex-col gap-6">
                                
                                {/* 댓글 작성 폼 */}
                                <div className="flex gap-4 items-start">
                                    {/* 아바타 아이콘 */}
                                    <div className="flex items-center justify-center size-10 rounded-full bg-slate-200 dark:bg-slate-800 text-slate-500 dark:text-slate-400 flex-shrink-0" data-alt="Current user icon">
                                        <MaterialSymbol name="person" className="user-icon" />
                                    </div>
                                    {/* 입력 필드 */}
                                    <div className="flex-1">
                                        <textarea 
                                            className="w-full rounded-lg border border-slate-300 dark:border-slate-700 bg-background-light dark:bg-background-dark p-3 text-sm text-slate-800 dark:text-slate-300 placeholder-slate-400 dark:placeholder-slate-500 focus:ring-2 focus:ring-primary focus:border-primary" 
                                            placeholder="Add a comment..." 
                                            rows="3"
                                        />
                                        <button className="mt-2 flex max-w-[480px] cursor-pointer items-center justify-center overflow-hidden rounded-lg h-10 px-5 bg-primary text-white text-sm font-bold leading-normal tracking-[0.015em] hover:opacity-90 transition-opacity">
                                            <span>Submit</span>
                                        </button>
                                    </div>
                                </div>
                                
                                <hr className="border-border-light dark:border-slate-800"/>
                                
                                {/* 댓글 목록 */}
                                <ul className="flex flex-col gap-6">
                                    {comments.map(comment => (
                                        <li key={comment.id} className="comment-card flex gap-4 items-start">
                                            {/* 아바타 아이콘 */}
                                            <div className="flex items-center justify-center size-10 rounded-full bg-slate-200 dark:bg-slate-800 text-slate-500 dark:text-slate-400 flex-shrink-0" data-alt="Commenter icon">
                                                <MaterialSymbol name="person" className="user-icon" />
                                            </div>
                                            <div className="flex-1">
                                                <div className="flex items-center gap-2 mb-1">
                                                    {/* 작성자 및 날짜 */}
                                                    <p className="text-sm font-bold text-slate-800 dark:text-slate-200">{comment.author}</p>
                                                    <p className="text-xs text-slate-500 dark:text-slate-400">·</p>
                                                    <p className="text-xs text-slate-500 dark:text-slate-400">{comment.date}</p>
                                                </div>
                                                {/* 댓글 내용 */}
                                                <p className="text-sm text-slate-700 dark:text-slate-300">{comment.text}</p>
                                            </div>
                                        </li>
                                    ))}
                                </ul>
                            </div>
                        </div>
                    </div>
                </div>
            </main>
        </div>
    );
};

export default PostDetailPage;