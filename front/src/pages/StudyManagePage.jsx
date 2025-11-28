import React, { useState } from 'react';
import { useParams } from 'react-router-dom';
import { patchStudyForm } from '../hooks/useStudyForm';
import Header from '../components/ui/Header';
import MaterialSymbol from '../components/ui/MaterialSymbol';
import { mockStudyDetail, mockApplications, mockCategories } from '../data/studyData';
import { Link } from 'react-router-dom';
import { getStudyDetail, getStudyMember, approveApplicant, rejectApplicant, deleteMember } from '../services/studyApi';

// ---------------------------------------------------------------------
// 탭 컴포넌트 1: 스터디 정보 수정 폼
// ---------------------------------------------------------------------
const StudyInfoTab = ({ study }) => {
    // 이전 스터디 생성 페이지의 폼을 재활용합니다.
    const mockTags = ["React", "Next.js", "Frontend"];
    const { 
            formData, 
            handleChange, 
            handleSubmit, 
            isSubmitting 
        } = patchStudyForm(study); 

    return (
        <form 
            className="space-y-6 mt-6"
            onSubmit={handleSubmit}
        >
            
            {/* 1. 스터디 제목 */}
            <div>
                <label className="block text-sm font-medium text-text-light-secondary dark:text-text-dark-secondary" htmlFor="study-title">스터디 제목</label>
                <div className="mt-1">
                    <input 
                        className="block w-full rounded-md border-border-light dark:border-border-dark shadow-sm focus:ring-primary focus:border-primary sm:text-sm bg-background-light dark:bg-background-dark text-text-light-primary dark:text-text-dark-primary" 
                        id="study-title" 
                        name="title" 
                        type="text"
                        value={formData.title}
                        onChange={handleChange}
                    />
                </div>
            </div>

            {/* 2. 최대 인원 수 및 태그 (생략) */}
            {/* ... 스터디 생성 페이지와 동일한 인원 및 태그 입력 필드 ... */}

            {/* 3. 스터디 카테고리 (체크박스) */}
            <div>
                <fieldset>
                    <legend className="text-base font-medium text-text-light-secondary dark:text-text-dark-secondary">스터디 카테고리</legend>
                    <div className="mt-4 grid grid-cols-2 sm:grid-cols-3 gap-y-3 gap-x-4">
                        {mockCategories.filter(c => c !== '전체').map(category => (
                            <div key={category} className="flex items-center">
                                <input 
                                    className="h-4 w-4 rounded border-border-light dark:border-border-dark text-primary focus:ring-primary bg-background-light dark:bg-background-dark" 
                                    id={`cat-${category}`} 
                                    name="category" 
                                    type="radio"
                                    value={formData.category}
                                    onChange={handleChange}
                                />
                                <label className="ml-2 block text-sm text-text-light-primary dark:text-text-dark-primary cursor-pointer" htmlFor={`cat-${category}`}>{category}</label>
                            </div>
                        ))}
                    </div>
                </fieldset>
            </div>

            {/* 4. 스터디 설명 */}
            <div>
                <label className="block text-sm font-medium text-text-light-secondary dark:text-text-dark-secondary" htmlFor="study-description">스터디 설명</label>
                <div className="mt-1">
                    <textarea 
                        className="block w-full rounded-md border-border-light dark:border-border-dark shadow-sm focus:ring-primary focus:border-primary sm:text-sm bg-background-light dark:bg-background-dark text-text-light-primary dark:text-text-dark-primary" 
                        id="study-description" 
                        name="description" 
                        rows="8"
                        value={formData.description}
                        onChange={handleChange}
                    />
                </div>
            </div>

            {/* 저장 버튼 */}
            <div className="flex justify-end pt-4">
                <button 
                    type="submit" 
                    className="bg-primary text-white px-6 py-2 rounded-md text-sm font-semibold shadow-sm hover:opacity-90 transition-colors"
                >
                    정보 수정
                </button>
            </div>
        </form>
    );
};

// ---------------------------------------------------------------------
// 탭 컴포넌트 2: 구성원 관리 (Kick 버튼 포함)
// ---------------------------------------------------------------------
const MemberManageTab = ({ members, studyId }) => {
    return (
        <div className="space-y-4 mt-6">
            {members.map((member, index) => {
                const isLeader = member.role === 'leader';
                return (
                    // Study Detail 페이지에서 사용한 MemberItem을 관리 기능에 맞게 수정
                    <div key={index} className="flex items-center justify-between p-3 bg-gray-50 dark:bg-gray-700/50 rounded-lg border border-border-light dark:border-border-dark">
                        <div className="flex items-center space-x-3">
                            <div className="w-8 h-8 rounded-full bg-slate-200 dark:bg-slate-700 flex items-center justify-center">
                                <MaterialSymbol name="person" className="text-slate-500 dark:text-slate-400 text-lg" style={{ fontSize: '1.25rem' }} />
                            </div>
                            <span className={`font-medium text-sm ${isLeader ? 'text-primary' : 'text-text-light-primary dark:text-text-dark-primary'}`}>
                                {member.name}
                            </span>
                            {isLeader && <span className="text-xs text-primary bg-primary/10 px-2 py-0.5 rounded-full font-bold">LEADER</span>}
                        </div>
                        
                        {/* 리더가 아닐 때만 추방 버튼 표시 */}
                        {!isLeader && (
                            <button className="px-3 py-1 text-xs font-medium text-red-600 dark:text-red-500 rounded-md border border-red-500/50 hover:bg-red-50 dark:hover:bg-red-500/10 transition-colors" onClick={() => deleteMember(studyId, member.name)}>
                                추방
                            </button>
                        )}
                    </div>
                );
            })}
        </div>
    );
};

// ---------------------------------------------------------------------
// 탭 컴포넌트 3: 참여 신청 관리
// ---------------------------------------------------------------------
const ApplicationManageTab = ({ applications, studyId }) => {
    return (
        <div className="space-y-4 mt-6">
            {applications.length === 0 ? (
                <p className="text-center py-8 text-text-light-secondary dark:text-text-dark-secondary">현재 새로운 참여 신청이 없습니다.</p>
            ) : (
                <ul className="divide-y divide-border-light dark:divide-border-dark bg-surface-light dark:bg-surface-dark rounded-lg shadow-sm border border-border-light dark:border-border-dark overflow-hidden">
                    {applications.map(app => (
                        <li key={app.id} className="px-6 py-4">
                            <div className="flex items-center justify-between">
                                <div className="flex items-center space-x-3">
                                    <div className="flex-shrink-0 w-8 h-8 flex items-center justify-center bg-gray-200 dark:bg-zinc-700 rounded-full">
                                        <MaterialSymbol name="person" className="text-gray-500 dark:text-gray-400 text-lg" style={{ fontSize: '1.25rem' }} />
                                    </div>
                                    <p className="text-sm font-medium text-text-light-primary dark:text-text-dark-primary">{app.applicant}</p>
                                    <p className="text-xs text-text-light-secondary dark:text-text-dark-secondary ml-4">{app.date} 신청</p>
                                </div>
                                <div className="flex items-center space-x-2">
                                    {/* 수락/거절 버튼 */}
                                    <button className="px-2.5 py-1 text-xs font-semibold text-primary border border-primary rounded-md hover:bg-primary/10 transition-colors" type="button" onClick={() => approveApplicant(studyId, app.id)}>수락</button>
                                    <button className="px-2.5 py-1 text-xs font-semibold text-text-light-secondary dark:text-text-dark-secondary border border-border-light dark:border-border-dark rounded-md hover:bg-gray-100 dark:hover:bg-gray-700 transition-colors" type="button" onClick={() => rejectApplicant(studyId, app.id)}>거절</button>
                                </div>
                            </div>
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
};

// ---------------------------------------------------------------------
// 메인 컴포넌트
// ---------------------------------------------------------------------
const StudyManagePage = () => {
    const { id } = useParams();
    const foundStudyDetail = mockStudyDetail.find(detail => detail.id === parseInt(id));
    //const foundStudyDetail = getStudyDetail(id);
    //const foundStudyMembers = getStudyMember(id);
    
        // 🌟 2. 해당 ID의 스터디가 없을 경우 처리 (예외 처리)
        if (!foundStudyDetail) {
            return (
                <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                    <h1 className="text-3xl font-bold tracking-tight text-red-500">
                        존재하지 않는 스터디: 스터디 ID {id}를 찾을 수 없습니다.
                    </h1>
                </main>
            );
        }

    const tabs = [
        { name: '스터디 정보 수정', component: <StudyInfoTab study={foundStudyDetail} /> },
        { name: '구성원 관리', component: <MemberManageTab members={foundStudyDetail.members} studyId={id} /> },
        //{ name: '구성원 관리', component: <MemberManageTab members={foundStudyMembers} studyId={id} /> },     백엔드 연동시 교체
        { name: '참여 신청 관리', component: <ApplicationManageTab applications={mockApplications} studyId={id}/> },
        //{ name: '참여 신청 관리', component: <ApplicationManageTab applications={foundStudyDetail.applicants} studyId={id} /> },   //백엔드 연동 시 이걸로 교체
    ];
    
    // 기본 탭을 '스터디 정보 수정'으로 설정
    const [activeTab, setActiveTab] = useState(tabs[0].name);

    return (
        <div className="min-h-screen bg-background-light dark:bg-background-dark font-display antialiased">
            <Header /> 
            
            <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                {/* 헤더 영역 */}
                <div className="flex justify-between items-center mb-6">
                    <h1 className="text-3xl font-bold tracking-tight text-text-light-primary dark:text-text-dark-primary">
                        {foundStudyDetail.title} 관리
                    </h1>
                    <Link to={`/study/${id}`} className="text-sm font-medium text-primary hover:text-primary/80 transition-colors">
                        스터디 메인으로 돌아가기 &rarr;
                    </Link>
                </div>
                
                {/* 탭 구조 */}
                <div className="max-w-4xl mx-auto">
                    
                    {/* 탭 네비게이션 */}
                    <nav className="flex space-x-4 border-b border-border-light dark:border-border-dark mb-6">
                        {tabs.map((tab) => (
                            <button
                                key={tab.name}
                                onClick={() => setActiveTab(tab.name)}
                                className={`py-2 px-1 text-sm font-medium transition-colors ${
                                    activeTab === tab.name
                                        ? 'border-b-2 border-primary text-primary dark:text-white font-bold'
                                        : 'border-b-2 border-transparent text-text-light-secondary dark:text-text-dark-secondary hover:text-primary'
                                }`}
                            >
                                {tab.name}
                            </button>
                        ))}
                    </nav>

                    {/* 탭 콘텐츠 */}
                    <div className="bg-surface-light dark:bg-surface-dark p-6 sm:p-8 rounded-lg shadow-xl border border-border-light dark:border-border-dark">
                        {tabs.find(tab => tab.name === activeTab)?.component}
                    </div>
                </div>
            </main>
        </div>
    );
};

export default StudyManagePage;