// src/pages/LoginPage.jsx
import { useEffect, useState } from "react";
import { useNavigate } from "react-router-dom";
import { supabase } from "../supabaseClient.js"; 

export default function LoginPage() {
  const [loading, setLoading] = useState(true);
  const navigate = useNavigate();

  // 이미 로그인된 경우 바로 홈으로 리다이렉트
  useEffect(() => {
    const checkSession = async () => {
      const { data } = await supabase.auth.getSession();
      if (data.session) {
        navigate("/"); // 홈으로 이동
      } else {
        setLoading(false);
      }
    };

    checkSession();

    // 실시간 인증 상태 감지 (GitHub 로그인 완료 후 자동 이동)
    const {
      data: { subscription },
    } = supabase.auth.onAuthStateChange((event, session) => {
      if (event === "SIGNED_IN" && session) {
        navigate("/");
      }
    });

    // 클린업
    return () => {
      subscription.unsubscribe();
    };
  }, [navigate]);

  const signInWithGithub = async () => {
    await supabase.auth.signInWithOAuth({
      provider: "github",
      options: {
        redirectTo: window.location.origin, // 로그인 후 현재 도메인으로 돌아옴
      },
    });
  };

  if (loading) {
    return (
      <div className="min-h-screen flex items-center justify-center bg-gray-50 dark:bg-gray-900">
        <div className="text-2xl text-gray-600 dark:text-gray-300">확인 중...</div>
      </div>
    );
  }

  return (
    <div className="min-h-screen bg-gray-50 dark:bg-gray-900 flex items-center justify-center py-12 px-6">
      <div className="max-w-md w-full space-y-10 text-center">
        {/* 로고 & 타이틀 */}
        <div>
          <h1 className="text-6xl font-black text-transparent bg-clip-text bg-gradient-to-r from-blue-600 to-purple-600 mb-4">
            Gitrajabi
          </h1>
          <p className="text-xl text-gray-600 dark:text-gray-400">
            함께 공부하는 가장 쉬운 방법
          </p>
        </div>

        {/* GitHub 로그인 버튼 */}
        <button
          onClick={signInWithGithub}
          className="w-full flex items-center justify-center gap-4 px-8 py-5 bg-black hover:bg-gray-800 text-white font-semibold text-lg rounded-2xl shadow-xl transition transform hover:scale-105 duration-200"
        >
          <svg className="w-8 h-8" fill="currentColor" viewBox="0 0 24 24">
            <path d="M12 0c-6.626 0-12 5.373-12 12 0 5.302 3.438 9.8 8.207 11.387.599.111.793-.261.793-.577v-2.234c-3.338.726-4.033-1.416-4.033-1.416-.546-1.387-1.333-1.756-1.333-1.756-1.089-.745.083-.729.083-.729 1.205.084 1.839 1.237 1.237 1.237 1.07 1.834 2.807 1.304 3.492.997.107-.775.418-1.305.762-1.604-2.665-.305-5.467-1.334-5.467-5.931 0-1.311.469-2.381 1.236-3.221-.124-.303-.535-1.524.117-3.176 0 0 1.008-.327 3.301 1.23.957-.266 1.983-.399 3.003-.404 1.02.005 2.047.138 3.006.404 2.291-1.552 3.297-1.23 3.297-1.23.653 1.653.242 2.874.118 3.176.77.84 1.235 1.911 1.235 3.221 0 4.609-2.807 5.624-5.479 5.921.43.372.823 1.102.823 2.222v3.293c0 .319.192.694.801.576 4.765-1.589 8.199-6.086 8.199-11.386 0-6.627-5.373-12-12-12z" />
          </svg>
          GitHub로 로그인하기
        </button>

        {/* 안내 문구 */}
        <div className="text-sm text-gray-500 dark:text-gray-400 space-y-2">
          <p>회원가입 없이도 바로 시작할 수 있어요</p>
          <p>GitHub 계정으로 로그인하면 공부 기록이 안전하게 저장됩니다</p>
        </div>
      </div>
    </div>
  );
}