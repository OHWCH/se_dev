import React from 'react';
import { BrowserRouter, Routes, Route } from "react-router-dom";
import Homepage from "../pages/HomePage"; 
import Mypage from "../pages/Mypage";
import Communitypage from "../pages/CommunityPage";
import Postwritepage from "../pages/PostWritePage";
import Studylistpage from "../pages/StudyListPage";
import StudyCreatePage from '../pages/StudyCreatePage';
import StudyDetailPage from "../pages/StudyDetailPage";
import StudyManagePage from "../pages/StudyManagePage";
import ChallengePage from "../pages/ChallengePage";


const Router = () => {
    return (
        
            <Routes>
                <Route path="/" element={<Homepage />} />
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