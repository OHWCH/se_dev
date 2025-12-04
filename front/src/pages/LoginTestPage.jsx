import React from 'react';
import axios from 'axios';

// 백엔드 API 엔드포인트
const GITHUB_AUTH_URL_API = 'http://3.35.246.234:8080/api/github/authorize-url';

/**
 * GitHub 로그인을 위한 리다이렉션 버튼 컴포넌트
 * 이 컴포넌트는 백엔드에서 생성된 GitHub OAuth URL을 받아와 사용자를 해당 URL로 이동시킵니다.
 */
const LoginTestPage = () => {
    
    /**
     * GitHub 로그인 버튼 클릭 핸들러
     * 1. 백엔드에 GitHub OAuth 인증 URL 요청
     * 2. 응답받은 URL로 브라우저 리다이렉트
     */
    const handleGitHubLogin = async () => {
        try {
            console.log('GitHub 로그인 URL 요청 중...');
            
            // 1. 백엔드 GET /api/github/authorize-url 호출
            const response = await axios.get(GITHUB_AUTH_URL_API);
            
            // 응답 데이터는 백엔드에서 반환한 GitHub OAuth URL 문자열
            const githubAuthUrl = response.data;
            
            if (githubAuthUrl) {
                console.log('받은 GitHub 인증 URL:', githubAuthUrl);
                
                // 2. 해당 URL로 브라우저 리다이렉트
                window.location.href = githubAuthUrl;
            } else {
                alert('GitHub 인증 URL을 받아오지 못했습니다.');
            }
            
        } catch (error) {
            console.error('GitHub 로그인 처리 중 오류 발생:', error);
            // 에러 메시지 표시 (예: 백엔드 서버가 켜져 있지 않거나 CORS 문제 등)
            alert('로그인 처리 중 오류가 발생했습니다. 콘솔을 확인해 주세요.');
        }
    };

    return (
        <div className="flex flex-col items-center justify-center min-h-screen bg-gray-100 dark:bg-gray-900">
            <div className="p-8 bg-white dark:bg-gray-800 rounded-lg shadow-xl w-full max-w-md text-center">
                <h2 className="text-2xl font-bold mb-6 text-gray-900 dark:text-white">
                    깃라잡이 로그인
                </h2>
                <p className="mb-8 text-gray-600 dark:text-gray-400">
                    GitHub 계정으로 간편하게 로그인하고 스터디에 참여하세요.
                </p>
                
                {/* 🌟 GitHub 로그인 버튼 */}
                <button
                    onClick={handleGitHubLogin}
                    className="w-full flex items-center justify-center px-4 py-3 border border-gray-300 rounded-lg shadow-sm text-sm font-medium text-gray-700 bg-white hover:bg-gray-50 dark:bg-gray-700 dark:text-white dark:border-gray-600 dark:hover:bg-gray-600 transition-colors"
                >
                    {/* 깃허브 아이콘 (Material Symbol 예시) */}
                    <svg 
                        className="w-5 h-5 mr-2" 
                        fill="currentColor" 
                        viewBox="0 0 20 20" 
                        xmlns="http://www.w3.org/2000/svg"
                    >
                        <path 
                            fillRule="evenodd" 
                            d="M10 0C4.477 0 0 4.484 0 10.017c0 4.421 2.865 8.16 6.839 9.48.5.092.682-.218.682-.483 0-.237-.008-.868-.013-1.703-2.782.605-3.37-1.34-3.37-1.34-.454-1.157-1.11-1.464-1.11-1.464-.908-.619.068-.606.068-.606 1.007.07 1.532 1.03 1.532 1.03.892 1.529 2.341 1.085 2.91.826.091-.64.35-1.085.634-1.334-2.224-.251-4.557-1.111-4.557-4.935 0-1.089.39-1.984 1.029-2.685-.103-.251-.446-1.272.098-2.65 0 0 .84-.27 2.75 1.025A9.575 9.575 0 0110 4.39a9.575 9.575 0 012.75-.375c1.91 1.295 2.75 1.025 2.75 1.025.546 1.378.202 2.399.099 2.65.64.701 1.029 1.596 1.029 2.685 0 3.832-2.336 4.679-4.566 4.92.359.31.678.921.678 1.855 0 1.334-.012 2.41-.012 2.748 0 .267.18.577.688.484C17.135 18.177 20 14.437 20 10.017A10.01 10.01 0 0010 0z" 
                            clipRule="evenodd" 
                        />
                    </svg>
                    GitHub으로 로그인
                </button>
            </div>
        </div>
    );
};

export default LoginTestPage;