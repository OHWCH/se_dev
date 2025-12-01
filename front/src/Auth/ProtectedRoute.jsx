// src/components/ProtectedRoute.jsx
import { useEffect, useState } from "react";
import { Navigate, Outlet } from "react-router-dom";
// import { supabase } from "../supabaseClient.js"; // Supabase 클라이언트 제거

/**
 * 로그인된 사용자만 접근할 수 있도록 경로를 보호하는 컴포넌트
 * 로컬 스토리지에 accessToken이 존재하는지 여부를 기준으로 인증을 확인합니다.
 * @returns {JSX.Element}
 */
export default function ProtectedRoute() {
  // null: 확인 중, true/false: 결과
  const [isAuthenticated, setIsAuthenticated] = useState(null); 
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    // 로컬 스토리지에서 토큰을 확인하는 작업은 동기적으로 바로 실행 가능합니다.
    const checkAuth = () => {
      // 1. 로컬 스토리지에서 accessToken을 가져옵니다.
      const accessToken = localStorage.getItem("accessToken");
      
      // 토큰이 존재하면 인증된 것으로 간주합니다.
      if (accessToken) {
        setIsAuthenticated(true);
      } else {
        setIsAuthenticated(false);
      }
      setLoading(false);
    };

    checkAuth();
  }, []); // 의존성 배열을 비워 최초 마운트 시에만 실행

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