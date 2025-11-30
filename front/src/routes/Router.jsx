import React from 'react';
import { BrowserRouter, Routes, Route } from "react-router-dom";
import ProtectedRoute from '../components/ProtectedRoute';
import Homepage from "../pages/HomePage"; 
import Mypage from "../pages/Mypage";
import Communitypage from "../pages/CommunityPage";
import Postwritepage from "../pages/PostWritePage";
import Studylistpage from "../pages/StudyListPage";
import StudyCreatePage from '../pages/StudyCreatePage';
import StudyDetailPage from "../pages/StudyDetailPage";
import StudyManagePage from "../pages/StudyManagePage";
import ChallengePage from "../pages/ChallengePage";
import LoginPage from '../pages/LoginPage';
import LoginTestPage from '../pages/LoginTestPage';


const Router = () => {
    return (
            <Routes>
                {/* 1. 보호되지 않는 경로: 로그인 페이지는 로그인 여부와 상관없이 접근 가능 */}
                <Route path="/" element={<Homepage />} />
                <Route path="/login" element={<LoginPage />} />
                <Route path="/logintest" element={<LoginTestPage />} />
                {/* 2. 보호된 경로 그룹 설정 */}
                {/* ProtectedRoute를 부모 엘리먼트로 설정합니다. */}
                
                    {/* ProtectedRoute의 자식으로 정의된 경로는 모두 로그인이 필요합니다. */}
                <Route path="/mypage" element={<Mypage />} />
                <Route path="/community" element={<Communitypage />} />
                <Route path="/postwrite" element={<Postwritepage />} />
                <Route path="/studylist" element={<Studylistpage />} />
                <Route path="/study/create" element={<StudyCreatePage/>}/>
                <Route path="/study/:id" element={<StudyDetailPage />} />
                <Route path="/study/:id/manage" element={<StudyManagePage />} />
                <Route path="/challenge" element={<ChallengePage />} />
            </Routes>
        
    );
};

export default Router;