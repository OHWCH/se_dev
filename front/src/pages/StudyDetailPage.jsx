import React from 'react';
import { useParams } from 'react-router-dom';
import Header from '../components/ui/Header';
import MaterialSymbol from '../components/ui/MaterialSymbol';
import { mockStudyDetail } from '../data/studyData';
import { Link } from 'react-router-dom';

// 컴포넌트 재사용을 위해 내부적으로 정의 (옵션)
const TaskItem = ({ task }) => (
    <div className="flex items-center justify-between py-3 border-b border-border-light dark:border-border-dark last:border-b-0">
        <p className="text-sm font-medium text-text-light-primary dark:text-text-dark-primary">{task.title}</p>
        <p className="text-xs text-text-light-secondary dark:text-text-dark-secondary">{task.dueDate}</p>
    </div>
);

const MemberItem = ({ member }) => {
    const isLeader = member.role === 'leader';
    return (
        <div className="flex items-center space-x-3 py-2 border-b border-border-light dark:border-border-dark last:border-b-0">
            <div className="w-8 h-8 rounded-full bg-slate-200 dark:bg-slate-700 flex items-center justify-center">
                <MaterialSymbol name="person" className="text-slate-500 dark:text-slate-400 text-lg" style={{ fontSize: '1.25rem' }} />
            </div>
            <span className={`font-medium text-sm ${isLeader ? 'text-primary' : 'text-text-light-primary dark:text-text-dark-primary'}`}>
                {member.name}
            </span>
            {isLeader && <span className="text-xs text-primary bg-primary/10 px-2 py-0.5 rounded-full font-bold">LEADER</span>}
        </div>
    );
};


const StudyDetailPage = () => {
    const { id } = useParams(); // 라우팅 파라미터에서 ID를 가져옵니다.
    //const study = mockStudyDetail[id]; // 임시로 Mock Data 사용

    // 🌟 1. Mock 데이터 배열에서 ID가 일치하는 스터디를 찾습니다.
    // URL에서 가져온 id는 문자열이므로, 숫자로 변환하여 비교합니다 (parseInt).
    const foundStudyDetail = mockStudyDetail.find(detail => detail.id === parseInt(id));

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

    // 진행률 Bar
    const progressBarWidth = `${foundStudyDetail.progress.completionRate}%`;

    return (
        <div className="min-h-screen bg-background-light dark:bg-background-dark font-display antialiased">
            <Header /> 
            
            <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                {/* 1. 스터디 헤더 영역 */}
                <div className="flex justify-between items-center mb-6 pb-4 border-b border-border-light dark:border-border-dark">
                    <h1 className="text-3xl font-bold tracking-tight text-text-light-primary dark:text-text-dark-primary">{foundStudyDetail.title}</h1>
                    
                    {/* 임시로 관리 페이지 버튼 (리더에게만 보인다고 가정) */}
                    <Link to={`/study/${id}/manage`} className="flex items-center justify-center bg-gray-100 dark:bg-gray-700 text-text-light-secondary dark:text-text-dark-secondary font-medium px-4 py-2 rounded-md text-sm hover:bg-gray-200 dark:hover:bg-gray-600 transition-colors whitespace-nowrap">
                        <MaterialSymbol name="settings" className="mr-2 text-base" />
                        스터디 관리
                    </Link>
                </div>

                {/* 2. 메인 콘텐츠 2단 레이아웃 */}
                <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
                    
                    {/* 좌측: 스터디 정보 및 메뉴 (1/3) */}
                    <div className="lg:col-span-1 space-y-8">
                        
                        {/* 스터디 개요 카드 */}
                        <div className="bg-surface-light dark:bg-surface-dark p-6 rounded-lg shadow-md border border-border-light dark:border-border-dark">
                            <h2 className="text-xl font-bold mb-4 text-text-light-primary dark:text-text-dark-primary">스터디 개요</h2>
                            <p className="text-sm font-medium text-primary mb-2">{foundStudyDetail.category}</p>
                            <p className="text-text-light-secondary dark:text-text-dark-secondary text-sm leading-relaxed">{foundStudyDetail.description}</p>
                            <div className="mt-4 pt-4 border-t border-border-light dark:border-border-dark text-sm">
                                <p className="flex items-center text-text-light-secondary dark:text-text-dark-secondary">
                                    <MaterialSymbol name="star" className="mr-2 text-base" />
                                    리더: {foundStudyDetail.leader}
                                </p>
                            </div>
                        </div>

                        {/* 메뉴 링크 (임시) */}
                        <div className="bg-surface-light dark:bg-surface-dark p-2 rounded-lg shadow-sm border border-border-light dark:border-border-dark">
                            <Link to={`/study/${id}`} className="flex items-center p-3 rounded-md text-sm font-medium bg-gray-100 dark:bg-gray-700 text-primary dark:text-white transition-colors">
                                <MaterialSymbol name="dashboard" className="mr-3 text-lg" />
                                메인 대시보드
                            </Link>
                            <Link to={`/study/${id}/tasks`} className="flex items-center p-3 rounded-md text-sm font-medium text-text-light-secondary dark:text-text-dark-secondary hover:bg-gray-50 dark:hover:bg-gray-700 transition-colors">
                                <MaterialSymbol name="checklist" className="mr-3 text-lg" />
                                할 일 목록
                            </Link>
                            <Link to={`/study/${id}/docs`} className="flex items-center p-3 rounded-md text-sm font-medium text-text-light-secondary dark:text-text-dark-secondary hover:bg-gray-50 dark:hover:bg-gray-700 transition-colors">
                                <MaterialSymbol name="folder" className="mr-3 text-lg" />
                                문서/자료
                            </Link>
                        </div>
                    </div>
                    
                    {/* 우측: 진행 현황 및 구성원 (2/3) */}
                    <div className="lg:col-span-2 space-y-8">
                        
                        {/* 1. 진행 현황 카드 */}
                        <div className="bg-surface-light dark:bg-surface-dark p-6 rounded-lg shadow-md border border-border-light dark:border-border-dark">
                            <h2 className="text-xl font-bold mb-4 text-text-light-primary dark:text-text-dark-primary">진행 현황</h2>
                            <div className="flex justify-between items-center mb-2">
                                <span className="text-sm font-medium text-text-light-secondary dark:text-text-dark-secondary">
                                    완료 {foundStudyDetail.progress.completedTasks} / {foundStudyDetail.progress.totalTasks}개
                                </span>
                                <span className="text-lg font-bold text-primary">{foundStudyDetail.progress.completionRate}%</span>
                            </div>
                            {/* 진행률 Bar */}
                            <div className="w-full bg-gray-200 dark:bg-gray-700 rounded-full h-2.5">
                                <div className="bg-primary h-2.5 rounded-full transition-all duration-500" style={{ width: progressBarWidth }}></div>
                            </div>
                        </div>

                        {/* 2. 다가오는 할 일 카드 */}
                        <div className="bg-surface-light dark:bg-surface-dark p-6 rounded-lg shadow-md border border-border-light dark:border-border-dark">
                            <h2 className="text-xl font-bold mb-4 text-text-light-primary dark:text-text-dark-primary">다가오는 할 일</h2>
                            <div className="divide-y divide-border-light dark:divide-border-dark">
                                {foundStudyDetail.upcomingTasks.map(task => (
                                    <TaskItem key={task.id} task={task} />
                                ))}
                            </div>
                            <div className="mt-4 text-right">
                                <Link to={`/study/${id}/tasks`} className="text-sm font-medium text-primary hover:text-primary/80 transition-colors">
                                    전체 할 일 목록 보기 &rarr;
                                </Link>
                            </div>
                        </div>

                        {/* 3. 구성원 목록 카드 */}
                        <div className="bg-surface-light dark:bg-surface-dark p-6 rounded-lg shadow-md border border-border-light dark:border-border-dark">
                            <h2 className="text-xl font-bold mb-4 text-text-light-primary dark:text-text-dark-primary">구성원 ({foundStudyDetail.members.length}명)</h2>
                            <div className="space-y-1">
                                {foundStudyDetail.members.map((member, index) => (
                                    <MemberItem key={index} member={member} />
                                ))}
                            </div>
                        </div>
                    </div>
                </div>
            </main>
        </div>
    );
};

export default StudyDetailPage;