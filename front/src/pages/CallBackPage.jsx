import React, { useEffect } from 'react';
import { useSearchParams, useNavigate } from 'react-router-dom';
import { getMyProfile } from '../services/userApi';

const CallbackPage = () => {
    const [searchParams] = useSearchParams();
    const navigate = useNavigate();

    useEffect(() => {
        // 1. URL 쿼리 파라미터에서 Access Token을 가져옵니다.
        // 백엔드 구현에 따라 'accessToken' 또는 다른 이름일 수 있습니다.
        const accessToken = searchParams.get('accessToken'); 

        if (accessToken) {
            console.log('Access Token 수신 완료:', accessToken);
            
            // 2. localStorage에 토큰 저장 (Study API에서 사용됨)
            localStorage.setItem('accessToken', accessToken);
            
            // 3. 홈 또는 스터디 목록 페이지로 이동
            const fetchUserProfile = async () => {
                try {
                    await getMyProfile();

                    // 5. 모든 정보 저장이 완료되면 페이지 이동
                    navigate('/studylist', { replace: true });

                } catch (error) {
                    console.error('프로필 정보 로드 실패:', error);
                    alert('로그인에 성공했지만 사용자 정보 로드에 실패했습니다. 다시 로그인해 주세요.');
                    localStorage.removeItem('accessToken');
                    navigate('/logintest');
                }
            };
            
            fetchUserProfile();
            
        } else {
            // 토큰을 받지 못했다면 로그인 실패 처리
            console.error('로그인에 실패했거나 토큰이 누락되었습니다.');
            alert('로그인 실패. 다시 시도해 주세요.');
            navigate('/login');
        }
    }, [searchParams, navigate]);

    return (
        <div className="flex justify-center items-center h-screen">
            <p>로그인 처리 중...</p>
        </div>
    );
};

export default CallbackPage;