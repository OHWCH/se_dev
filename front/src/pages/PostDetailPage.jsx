import React, { useState, useEffect, useMemo } from 'react';
import { useParams, useNavigate, Link } from 'react-router-dom';
import Header from '../components/ui/Header';
import MaterialSymbol from '../components/ui/MaterialSymbol';
import { getPostDetail, deletePost, createComment, deleteComment } from '../services/boardApi';
import { showToast } from '../utils/toast';
import ReactMarkdown from 'react-markdown';
import remarkGfm from 'remark-gfm';
import { Prism as SyntaxHighlighter } from 'react-syntax-highlighter';
import { vscDarkPlus } from 'react-syntax-highlighter/dist/esm/styles/prism';

// 마크다운 코드 렌더러
const CodeRenderer = ({ node, inline, className, children, ...props }) => {
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
    <code className={className} {...props}>
      {children}
    </code>
  );
};

const PostDetailPage = () => {
  const { postId } = useParams();
  const navigate = useNavigate();

  const [post, setPost] = useState(null);
  const [comments, setComments] = useState([]);
  const [loading, setLoading] = useState(true);
  const [commentText, setCommentText] = useState('');
  const [isSubmitting, setIsSubmitting] = useState(false);

  // ✅ 수정됨: 로컬 스토리지에서 깃허브 아이디와 토큰 가져오기
  const currentGithubId = localStorage.getItem('currentGithubId');
  const accessToken = localStorage.getItem('accessToken');
  const isLoggedIn = !!accessToken; // 토큰이 있으면 로그인 상태로 간주

  useEffect(() => {
    fetchPostDetail();
    // eslint-disable-next-line react-hooks/exhaustive-deps
  }, [postId]);

  const fetchPostDetail = async () => {
    setLoading(true);
    try {
      const data = await getPostDetail(postId);
      setPost(data);
      setComments(data.comments || []);
    } catch (err) {
      console.error(err);
      showToast.error('게시글을 불러오지 못했습니다');
      navigate('/community', { replace: true });
    } finally {
      setLoading(false);
    }
  };

  const handleDeletePost = async () => {
    if (!window.confirm('정말 게시글을 삭제하시겠습니까?')) return;
    try {
      await deletePost(postId);
      showToast.success('게시글이 삭제되었습니다');
      navigate('/community', { replace: true });
    } catch (err) {
      console.error(err);
      showToast.error('삭제 권한이 없거나 실패했습니다');
    }
  };

  const handleCommentSubmit = async (e) => {
    e.preventDefault();
    if (!commentText.trim()) {
      showToast.error('댓글 내용을 입력해주세요');
      return;
    }
    
    // ✅ 수정됨: 로그인 여부 체크 (isLoggedIn 사용)
    if (!isLoggedIn) {
      showToast.error('로그인이 필요합니다');
      return;
    }

    setIsSubmitting(true);
    try {
      const newComment = await createComment(postId, commentText.trim());
      setComments((prev) => [newComment, ...prev]); 
      setCommentText('');
      showToast.success('댓글이 등록되었습니다');
    } catch (err) {
      console.error(err);
      showToast.error('댓글 작성에 실패했습니다');
    } finally {
      setIsSubmitting(false);
    }
  };

  const handleDeleteComment = async (commentId) => {
    if (!window.confirm('댓글을 삭제하시겠습니까?')) return;
    try {
      await deleteComment(postId, commentId);
      setComments((prev) => prev.filter((c) => c.commentId !== commentId));
      showToast.success('댓글이 삭제되었습니다');
    } catch (err) {
      console.error(err);
      showToast.error('삭제 권한이 없습니다');
    }
  };

  const markdownComponents = useMemo(() => ({
    code: CodeRenderer
  }), []);

  // ✅ 수정됨: 게시글 작성자 본인 확인 로직 (Github ID 기준)
  // post.authorGithubId와 내 currentGithubId가 같으면 true
  const postIsAuthor = post && currentGithubId && post.authorGithubId === currentGithubId;

  if (loading) {
    return (
      <div className="min-h-screen bg-background-light dark:bg-background-dark font-display text-gray-800 dark:text-gray-200 antialiased">
        <Header activeLink="community" />
        <main className="container mx-auto px-4 py-20 text-center">
          <div className="animate-pulse text-xl">게시글을 불러오는 중...</div>
        </main>
      </div>
    );
  }

  if (!post) {
    return (
      <div className="min-h-screen bg-background-light dark:bg-background-dark font-display text-gray-800 dark:text-gray-200 antialiased">
        <Header activeLink="community" />
        <main className="container mx-auto px-4 py-20 text-center">
          <p className="text-lg text-red-500">게시글을 찾을 수 없습니다.</p>
          <Link to="/community" className="text-primary-600 hover:text-primary-700 mt-4 inline-block font-medium">
            목록으로 돌아가기
          </Link>
        </main>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-background-light dark:bg-background-dark font-display text-text-light-primary dark:text-text-dark-primary antialiased">
      <Header activeLink="community" />

      <main className="container mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="max-w-4xl mx-auto bg-white dark:bg-gray-800 shadow-xl rounded-lg overflow-hidden transition-colors duration-200">
          <div className="p-6 sm:p-8">
            {/* 게시글 메타 정보 */}
            <h1 className="text-3xl font-bold text-gray-900 dark:text-white mb-4 break-words">{post.title}</h1>
            <div className="flex flex-wrap items-center text-sm text-gray-500 dark:text-gray-400 border-b border-gray-200 dark:border-gray-700 pb-4 mb-6 gap-y-2">
              <span className="font-medium mr-4 flex items-center gap-1">
                <MaterialSymbol name="person" className="text-[18px]" />
                {post.authorGithubId || '익명'}
              </span>
              <span className="hidden sm:inline mr-4">·</span>
              <span className="mr-4">조회수 {post.viewCount}</span>
              <span className="hidden sm:inline mr-4">·</span>
              <span>{new Date(post.createdAt).toLocaleString('ko-KR')}</span>
            </div>

            {/* 게시글 내용 */}
            <div className="prose dark:prose-invert max-w-none text-gray-800 dark:text-gray-200 mb-10 min-h-[100px]">
              <ReactMarkdown 
                remarkPlugins={[remarkGfm]} 
                components={markdownComponents}
              >
                {post.content}
              </ReactMarkdown>
            </div>

            {/* 수정/삭제 버튼: 본인(Github ID 일치)일 때만 표시 */}
            {postIsAuthor && (
              <div className="flex justify-end gap-3 border-t border-gray-200 dark:border-gray-700 pt-6">
                <Link
                  to={`/community/edit/${post.postId}`}
                  className="px-4 py-2 text-sm font-medium text-primary-600 border border-primary-600 rounded-md hover:bg-primary-50 dark:hover:bg-gray-700 transition-colors"
                >
                  수정
                </Link>
                <button
                  onClick={handleDeletePost}
                  className="px-4 py-2 text-sm font-medium text-red-600 border border-red-600 rounded-md hover:bg-red-50 dark:hover:bg-gray-700 transition-colors"
                >
                  삭제
                </button>
              </div>
            )}

            {/* 댓글 영역 */}
            <div className="mt-10 border-t border-gray-200 dark:border-gray-700 pt-8">
              <h2 className="text-2xl font-semibold text-gray-900 dark:text-white mb-6 flex items-center gap-2">
                <MaterialSymbol name="comment" />
                댓글 <span className="text-primary-600 dark:text-primary-400">{comments.length}</span>
              </h2>

              {/* 댓글 작성 폼 */}
              <form onSubmit={handleCommentSubmit} className="mb-8 bg-gray-50 dark:bg-gray-700/50 p-4 rounded-lg border border-gray-100 dark:border-gray-700">
                <textarea
                  className="w-full p-3 border border-gray-300 dark:border-gray-600 rounded-md bg-white dark:bg-gray-800 text-gray-900 dark:text-gray-100 placeholder-gray-500 dark:placeholder-gray-400 focus:ring-2 focus:ring-primary-500 focus:border-transparent outline-none transition-all resize-y min-h-[80px]"
                  rows="3"
                  // ✅ 수정됨: isLoggedIn 상태에 따른 플레이스홀더 변경
                  placeholder={isLoggedIn ? '자유롭게 의견을 남겨주세요...' : '로그인 후 댓글을 작성할 수 있습니다.'}
                  value={commentText}
                  onChange={(e) => setCommentText(e.target.value)}
                  // ✅ 수정됨: isLoggedIn 상태에 따른 활성화 여부
                  disabled={!isLoggedIn || isSubmitting}
                />
                <div className="flex justify-end mt-3">
                  <button
                    type="submit"
                    // ✅ 수정됨: 버튼 활성화 조건 (isLoggedIn 포함)
                    disabled={!isLoggedIn || isSubmitting || !commentText.trim()}
                    className={`px-5 py-2 text-sm font-medium rounded-md transition-all ${
                      isLoggedIn && !isSubmitting && commentText.trim()
                        ? 'bg-blue-600 text-white hover:bg-primary-700 shadow-sm'
                        : 'bg-gray-300 text-gray-500 dark:bg-gray-600 dark:text-gray-400 cursor-not-allowed'
                    }`}
                  >
                    {isSubmitting ? '등록 중...' : '댓글 등록'}
                  </button>
                </div>
              </form>

              {/* 댓글 목록 */}
              <div className="space-y-4">
                {comments.length === 0 ? (
                  <div className="text-center py-10 bg-gray-50 dark:bg-gray-800/50 rounded-lg border border-dashed border-gray-300 dark:border-gray-700">
                    <p className="text-gray-500 dark:text-gray-400">아직 작성된 댓글이 없습니다.</p>
                    <p className="text-sm text-gray-400 mt-1">첫 번째 댓글의 주인공이 되어보세요!</p>
                  </div>
                ) : (
                  comments.map((comment) => (
                    <div
                      key={comment.commentId}
                      className="flex gap-4 p-4 border border-gray-100 dark:border-gray-700 rounded-lg bg-white dark:bg-gray-800 hover:border-gray-200 dark:hover:border-gray-600 transition-colors"
                    >
                      {/* 프로필 아이콘 */}
                      <div className="flex-shrink-0 w-10 h-10 rounded-full bg-slate-200 dark:bg-slate-700 flex items-center justify-center text-slate-500 dark:text-slate-300">
                        <MaterialSymbol name="person" className="text-[20px]" />
                      </div>

                      <div className="flex-1 min-w-0">
                        <div className="flex items-center justify-between mb-1">
                          <div className="flex items-center gap-2">
                            <span className="font-semibold text-gray-900 dark:text-gray-100 text-sm">
                              {comment.authorGithubId || '익명'}
                            </span>
                            <span className="text-xs text-slate-400">
                              {new Date(comment.createdAt).toLocaleString('ko-KR')}
                            </span>
                          </div>

                          {/* ✅ 수정됨: 댓글 삭제 버튼 (Github ID 기준 본인 확인) */}
                          {currentGithubId && comment.authorGithubId === currentGithubId && (
                            <button
                              onClick={() => handleDeleteComment(comment.commentId)}
                              className="text-gray-400 hover:text-red-500 text-sm transition-colors p-1"
                              title="삭제"
                            >
                              <MaterialSymbol name="delete" className="text-[18px]" />
                            </button>
                          )}
                        </div>

                        <p className="text-sm text-gray-700 dark:text-gray-300 whitespace-pre-wrap break-words leading-relaxed">
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