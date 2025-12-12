// src/router/Router.jsx (또는 App.jsx에 있는 부분)
import React from 'react';
import { Routes, Route, Link } from "react-router-dom";
import ProtectedRoute from '../Auth/ProtectedRoute';

// 페이지 임포트
import Homepage from "../pages/HomePage"; 
import Mypage from "../pages/Mypage";
import CommunityPage from "../pages/CommunityPage";
import PostWritePage from "../pages/PostWritePage";
import PostDetailPage from "../pages/PostDetailPage";
import PostEditPage from '../pages/PostEditPage';        
import StudyListPage from "../pages/StudyListPage";
import StudyCreatePage from '../pages/StudyCreatePage';
import StudyDetailPage from "../pages/StudyDetailPage";
import StudyManagePage from "../pages/StudyManagePage";
import ChallengePage from "../pages/ChallengePage";
import LoginPage from '../pages/LoginPage';
import LoginTestPage from '../pages/LoginTestPage';
import CallbackPage from '../pages/CallBackPage';

const Router = () => {
    return (
        <Routes>
            {/* 공개 라우트 (로그인 안 해도 됨) */}
            <Route path="/" element={<Homepage />} />
            <Route path="/login" element={<LoginPage />} />
            <Route path="/logintest" element={<LoginTestPage />} />
            <Route path="/callback" element={<CallbackPage />} />
            
            <Route path="/community" element={<CommunityPage />} />
            <Route path="/community/post/:postId" element={<PostDetailPage />} />  
            
            <Route path="/studylist" element={<StudyListPage />} />

            {/* 보호된 라우트 (로그인 필수) */}
            <Route element={<ProtectedRoute />}>
                <Route path="/mypage" element={<Mypage />} />
                
                <Route path="/postwrite" element={<PostWritePage />} />
                <Route path="/community/edit/:postId" element={<PostEditPage />} />
                <Route path="/study/create" element={<StudyCreatePage />} />
                <Route path="/study/:id" element={<StudyDetailPage />} />
                <Route path="/study/:id/manage" element={<StudyManagePage />} />
                <Route path="/challenge" element={<ChallengePage />} />
            </Route>

            {/* 404 페이지 (선택사항) */}
            <Route path="*" element={
                <div className="min-h-screen flex items-center justify-center">
                    <div className="text-center">
                        <h1 className="text-6xl font-bold text-gray-300 mb-4">404</h1>
                        <p className="text-xl text-gray-600">페이지를 찾을 수 없습니다</p>
                        <Link to="/" className="text-primary hover:underline mt-4 inline-block">홈으로 돌아가기</Link>
                    </div>
                </div>
            } />
        </Routes>
    );
};

export default Router;