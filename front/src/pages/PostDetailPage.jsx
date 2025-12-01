// src/pages/PostDetailPage.jsx
import React, { useState, useEffect } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import Header from '../components/ui/Header';
import MaterialSymbol from '../components/ui/MaterialSymbol';
import { getPostDetail, deletePost, createComment, deleteComment } from '../services/boardApi';
import { showToast } from '../utils/toast';
import ReactMarkdown from 'react-markdown';
import remarkGfm from 'remark-gfm';
import { Prism as SyntaxHighlighter } from 'react-syntax-highlighter';
import { vscDarkPlus } from 'react-syntax-highlighter/dist/esm/styles/prism';

const PostDetailPage = () => {
  const { postId } = useParams(); // URL에서 postId 가져오기
  const navigate = useNavigate();

  const [post, setPost] = useState(null);
  const [comments, setComments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [commentText, setCommentText] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);

  // 현재 로그인한 사용자 ID (예: oauthUser.id)
  // 실제로는 localStorage나 context에서 가져와야 함
  const currentUserId = localStorage.getItem('userId'); // 임시로 예시

  useEffect(() => {
    fetchPostDetail();
  }, [postId]);

  const fetchPostDetail = async () => {
    setLoading(true);
    try {
      const data = await getPostDetail(postId);
      setPost(data);
      setComments(data.comments || []); // 백엔드가 comments 포함해서 줌
    } catch (err) {
      showToast.error('게시글을 불러오지 못했습니다');
      console.error(err);
      navigate('/community');
    } finally {
      setLoading(false);
    }
  };

  const handleDeletePost = async () => {
    if (!window.confirm('정말 삭제하시겠습니까?')) return;

    try {
      await deletePost(postId);
      showToast.success('게시글이 삭제되었습니다');
      navigate('/community');
    } catch (err) {
      showToast.error('삭제 권한이 없거나 실패했습니다');
    }
  };

  const handleCreateComment = async () => {
    if (!commentText.trim()) {
      showToast.error('댓글 내용을 입력해주세요');
      return;
    }

    setIsSubmitting(true);
    try {
      const newComment = await createComment(postId, commentText.trim());
      setComments(prev => [newComment, ...prev]);
      setCommentText('');
      showToast.success('댓글이 작성되었습니다');
    } catch (err) {
      showToast.error('댓글 작성에 실패했습니다');
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleDeleteComment = async (commentId) => {
    if (!window.confirm('댓글을 삭제하시겠습니까?')) return;

    try {
      await deleteComment(postId, commentId);
      setComments(prev => prev.filter(c => c.id !== commentId));
      showToast.success('댓글이 삭제되었습니다');
    } catch (err) {
      showToast.error('삭제 권한이 없습니다');
    }
  };

  if (loading) {
    return (
      <div className="min-h-screen bg-background-light dark:bg-background-dark">
        <Header />
        <div className="container mx-auto px-4 py-20 text-center">
          <p className="text-xl">게시글을 불러오는 중...</p>
        </div>
      </div>
    );
  }

  if (!post) return null;

  return (
    <div className="relative flex h-auto min-h-screen w-full flex-col bg-background-light dark:bg-background-dark font-display text-text-light-primary dark:text-text-dark-primary antialiased">
      <Header />

      <main className="layout-container flex h-full grow flex-col">
        <div className="container mx-auto px-4 flex flex-1 justify-center py-8">
          <div className="layout-content-container flex flex-col max-w-4xl w-full flex-1 gap-6">

            {/* Breadcrumbs */}
            <div className="flex flex-wrap gap-2 items-center text-sm">
              <Link to="/" className="text-primary hover:underline">Home</Link>
              <span className="text-slate-400 dark:text-slate-500">/</span>
              <Link to="/community" className="text-primary hover:underline">커뮤니티</Link>
              <span className="text-slate-400 dark:text-slate-500">/</span>
              <span className="text-slate-800 dark:text-slate-300 font-medium">게시글</span>
            </div>

            {/* Post Content */}
            <div className="bg-surface-light dark:bg-surface-dark p-6 sm:p-8 rounded-xl border border-border-light dark:border-slate-800 shadow-sm">
              <div className="flex flex-col sm:flex-row sm:items-start sm:justify-between gap-4 mb-6">
                <div className="flex-1">
                  <h1 className="text-3xl font-bold text-slate-900 dark:text-white mb-2">
                    {post.title}
                  </h1>
                  <p className="text-sm text-slate-500 dark:text-slate-400">
                    작성자: {post.authorName || '익명'} · 조회수: {post.views?.toLocaleString() || 0} · {new Date(post.createdAt).toLocaleDateString('ko-KR')}
                  </p>
                </div>

                {/* 수정/삭제 버튼 (본인 글일 때만 표시) */}
                {currentUserId && post.authorId == currentUserId && (
                  <div className="flex gap-2">
                    <Link
                      to={`/community/edit/${postId}`}
                      className="px-4 py-2 bg-primary/20 dark:bg-primary/30 text-primary rounded-lg hover:bg-primary/30 transition"
                    >
                      수정
                    </Link>
                    <button
                      onClick={handleDeletePost}
                      className="px-4 py-2 bg-transparent text-red-500 hover:bg-red-500/10 rounded-lg transition"
                    >
                      삭제
                    </button>
                  </div>
                )}
              </div>

              <hr className="my-8 border-border-light dark:border-slate-800" />

              {/* 마크다운 본문 렌더링 */}
              <article className="prose prose-lg prose-slate dark:prose-invert max-w-none">
                <ReactMarkdown
                  remarkPlugins={[remarkGfm]}
                  components={{
                    code({ node, inline, className, children, ...props }) {
                      const match = /language-(\w+)/.exec(className || '');
                      return !inline && match ? (
                        <SyntaxHighlighter
                          style={vscDarkPlus}
                          language={match[1]}
                          PreTag="div"
                          {...props}
                        >
                          {String(children).replace(/\n$/, '')}
                        </SyntaxHighlighter>
                      ) : (
                        <code className="px-1.5 py-0.5 bg-slate-200 dark:bg-slate-700 rounded text-sm" {...props}>
                          {children}
                        </code>
                      );
                    }
                  }}
                >
                  {post.content}
                </ReactMarkdown>
              </article>
            </div>

            {/* 댓글 영역 */}
            <div className="bg-surface-light dark:bg-surface-dark p-6 sm:p-8 rounded-xl border border-border-light dark:border-slate-800 shadow-sm">
              <h2 className="text-xl font-bold mb-6">댓글 {comments.length}개</h2>

              {/* 댓글 작성 */}
              <div className="flex gap-4 items-start mb-8">
                <div className="size-10 rounded-full bg-slate-200 dark:bg-slate-800 flex items-center justify-center flex-shrink-0">
                  <MaterialSymbol name="person" />
                </div>
                <div className="flex-1">
                  <textarea
                    value={commentText}
                    onChange={(e) => setCommentText(e.target.value)}
                    placeholder="댓글을 작성하세요..."
                    rows="3"
                    className="w-full rounded-lg border border-slate-300 dark:border-slate-700 bg-background-light dark:bg-background-dark p-3 text-sm focus:ring-2 focus:ring-primary focus:border-primary outline-none"
                  />
                  <button
                    onClick={handleCreateComment}
                    disabled={isSubmitting || !commentText.trim()}
                    className="mt-3 px-5 py-2.metadata2 bg-primary text-white rounded-lg hover:opacity-90 disabled:opacity-50 transition"
                  >
                    {isSubmitting ? '작성 중...' : '댓글 작성'}
                  </button>
                </div>
              </div>

              <hr className="my-6 border-border-light dark:border-slate-800" />

              {/* 댓글 목록 */}
              <div className="space-y-6">
                {comments.length === 0 ? (
                  <p className="text-center text-slate-500 py-8">아직 댓글이 없습니다. 첫 댓글을 작성해보세요!</p>
                ) : (
                  comments.map(comment => (
                    <div key={comment.id} className="flex gap-4">
                      <div className="size-10 rounded-full bg-slate-200 dark:bg-slate-800 flex items-center justify-center flex-shrink-0">
                        <MaterialSymbol name="person" />
                      </div>
                      <div className="flex-1">
                        <div className="flex items-center justify-between">
                          <div>
                            <span className="font-semibold text-sm">{comment.authorName || '익명'}</span>
                            <span className="text-xs text-slate-500 ml-2">
                              {new Date(comment.createdAt).toLocaleString('ko-KR')}
                            </span>
                          </div>
                          {/* 본인 댓글일 때만 삭제 버튼 */}
                          {currentUserId && comment.authorId == currentUserId && (
                            <button
                              onClick={() => handleDeleteComment(comment.id)}
                              className="text-red-500 text-sm hover:underline"
                            >
                              삭제
                            </button>
                          )}
                        </div>
                        <p className="mt-2 text-sm text-slate-700 dark:text-slate-300">
                          {comment.content}
                        </p>
                      </div>
                    </div>
                  ))
                )}
              </div>
            </div>
          </div>
        </div>
      </main>
    </div>
  );
};

export default PostDetailPage;