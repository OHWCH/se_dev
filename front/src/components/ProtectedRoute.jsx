// src/components/ProtectedRoute.jsx
import { useEffect, useState } from "react";
import { Navigate, Outlet } from "react-router-dom";
import { supabase } from "../supabaseClient.js"; // supabase 클라이언트 가져오기

/**
 * 로그인된 사용자만 접근할 수 있도록 경로를 보호하는 컴포넌트
 * @param {object} props - 자식 컴포넌트 (렌더링할 페이지)
 * @returns {JSX.Element}
 */
export default function ProtectedRoute() {
  const [isAuthenticated, setIsAuthenticated] = useState(null); // null: 확인 중, true/false: 결과
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    const checkAuth = async () => {
      // 1. 현재 세션 확인
      const { data } = await supabase.auth.getSession();
      
      if (data.session) {
        setIsAuthenticated(true);
      } else {
        setIsAuthenticated(false);
      }
      setLoading(false);
    };

    checkAuth();
  }, []);

  // 1. 인증 상태 확인 중...
  if (loading || isAuthenticated === null) {
    // 로딩 상태를 표시하여 깜빡임을 방지
    return (
      <div className="min-h-screen flex items-center justify-center bg-gray-50 dark:bg-gray-900">
        <div className="text-2xl text-gray-600 dark:text-gray-300">인증 확인 중...</div>
      </div>
    );
  }

  // 2. 인증 성공: 요청한 자식 컴포넌트(페이지)를 렌더링
  if (isAuthenticated) {
    // Outlet은 ProtectedRoute가 감싸는 자식 라우트를 렌더링합니다.
    return <Outlet />; 
  }

  // 3. 인증 실패: 로그인 페이지로 강제 리디렉션
  return <Navigate to="/login" replace />;
}