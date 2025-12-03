import React, { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import Header from '../components/ui/Header';
import { getPostDetail, updatePost } from '../services/boardApi';
import { showToast } from '../utils/toast';

const PostEditPage = () => {
  const { postId } = useParams();
  const navigate = useNavigate();

  const [title, setTitle] = useState('');
  const [content, setContent] = useState('');
  const [loading, setLoading] = useState(true);
  const [isSubmitting, setIsSubmitting] = useState(false);

  // ✅ 수정됨: 로컬 스토리지에서 깃허브 아이디 가져오기
  const currentGithubId = localStorage.getItem('currentGithubId');

  useEffect(() => {
    const fetchPost = async () => {
      try {
        const data = await getPostDetail(postId);

        // ✅ 수정됨: 작성자 본인 확인 로직 (Github ID 기준)
        // 백엔드에서 authorGithubId를 보내준다고 가정 (PostResponse DTO 확인됨)
        if (!currentGithubId || data.authorGithubId !== currentGithubId) {
          showToast.error('수정 권한이 없습니다.');
          navigate(`/community/post/${postId}`);
          return;
        }

        setTitle(data.title || '');
        setContent(data.content || '');
      } catch (err) {
        showToast.error('게시글을 불러오지 못했습니다');
        navigate('/community');
      } finally {
        setLoading(false);
      }
    };
    fetchPost();
  }, [postId, navigate, currentGithubId]); // ✅ 수정됨: 의존성 배열에 currentGithubId 사용

  const handleUpdate = async () => {
    if (!title.trim() || !content.trim()) {
      showToast.error('제목과 내용을 입력해주세요');
      return;
    }

    setIsSubmitting(true);
    try {
      await updatePost(postId, { title, content });
      showToast.success('게시글이 수정되었습니다');
      navigate(`/community/post/${postId}`);
    } catch (err) {
      showToast.error('게시글 수정에 실패했습니다');
    } finally {
      setIsSubmitting(false);
    }
  };

  if (loading) {
    return (
      <div className="min-h-screen bg-background-light dark:bg-background-dark font-display text-gray-800 dark:text-gray-200 antialiased">
        <Header activeLink="community" />
        <main className="container mx-auto px-4 py-20 text-center">
          <p className="text-xl">게시글을 불러오는 중...</p>
        </main>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-background-light dark:bg-background-dark font-display text-text-light-primary dark:text-text-dark-primary antialiased">
      <Header activeLink="community" />

      <main className="container mx-auto px-4 sm:px-6 lg:px-8 py-8">
        <div className="max-w-4xl mx-auto bg-white dark:bg-gray-800 shadow-xl rounded-lg overflow-hidden">
          <div className="p-6 sm:p-8">
            <h1 className="text-3xl font-bold text-gray-900 dark:text-white mb-6">게시글 수정</h1>

            <div className="space-y-6">
              <input
                type="text"
                value={title}
                onChange={(e) => setTitle(e.target.value)}
                placeholder="제목을 입력하세요"
                className="w-full border border-slate-300 dark:border-slate-700 rounded-lg p-3 text-lg bg-background-light dark:bg-background-dark text-gray-900 dark:text-gray-100 focus:ring-2 focus:ring-primary outline-none"
              />

              <textarea
                value={content}
                onChange={(e) => setContent(e.target.value)}
                placeholder="내용을 입력하세요"
                rows="12"
                className="w-full border border-slate-300 dark:border-slate-700 rounded-lg p-3 text-sm bg-background-light dark:bg-background-dark text-gray-900 dark:text-gray-100 focus:ring-2 focus:ring-primary outline-none"
              />

              <div className="flex gap-3 justify-end pt-4 border-t">
                <button
                  onClick={handleUpdate}
                  disabled={isSubmitting}
                  className="px-6 py-2 text-sm font-medium bg-primary text-white rounded-lg hover:opacity-90 disabled:opacity-50 transition"
                >
                  {isSubmitting ? '수정 중...' : '수정 완료'}
                </button>
                <button
                  onClick={() => navigate(`/community/post/${postId}`)}
                  className="px-6 py-2 text-sm font-medium text-gray-700 dark:text-gray-300 border border-gray-300 dark:border-gray-600 rounded-lg hover:bg-gray-100 dark:hover:bg-gray-700 transition"
                >
                  취소
                </button>
              </div>
            </div>
          </div>
        </div>
      </main>
    </div>
  );
};

export default PostEditPage;