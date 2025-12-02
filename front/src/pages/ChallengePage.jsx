import React, { useState, useEffect, useMemo } from 'react'; // 🌟 useMemo 추가
import Header from '../components/ui/Header';
import MaterialSymbol from '../components/ui/MaterialSymbol';
// 🌟 API 함수 임포트 (challengeApi 파일에 있다고 가정)
import { getContribution } from '../services/userApi'; 

// ====================================================================
// 🌟 1. 로컬 도전과제 목표 정의 (API 통계에 매핑)
// ====================================================================
const ALL_CHALLENGES_GOALS = [
    // 초급 - 커밋
    { id: 'b1', type: 'commit', level: 'BEGINNER', title: '커밋 100회 완료', description: '총 100번의 커밋을 완료해보세요!', total: 100 },
    { id: 'b2', type: 'commit', level: 'BEGINNER', title: '커밋 300회 완료', description: '총 300번의 커밋을 완료해보세요!', total: 300 },
    { id: 'b3', type: 'commit', level: 'BEGINNER', title: '커밋 500회 완료', description: '총 500개의 커밋을 완료해보세요!', total: 500 },

    // 중급 - PR (Pull Request)
    { id: 'i1', type: 'pr', level: 'INTERMEDIATE', title: 'PR 10회 완료', description: '총 10번의 Pull Request를 완료해보세요!', total: 10 },
    { id: 'i2', type: 'pr', level: 'INTERMEDIATE', title: 'PR 30회 완료', description: '총 30번의 Pull Request를 완료해보세요!', total: 30 },
    { id: 'i3', type: 'pr', level: 'INTERMEDIATE', title: 'PR 50회 완료', description: '총 50번의 Pull Request를 완료해보세요!', total: 50 },
    
    // 중급 - 이슈
    { id: 'i4', type: 'issue', level: 'INTERMEDIATE', title: '이슈 5회 등록', description: '총 5번의 이슈를 완료해보세요!', total: 5 },
    { id: 'i5', type: 'issue', level: 'INTERMEDIATE', title: '이슈 30회 등록', description: '총 30번의 이슈를 완료해보세요!', total: 30 },
    { id: 'i6', type: 'issue', level: 'INTERMEDIATE', title: '이슈 50회 등록', description: '총 50번의 이슈를 완료해보세요!', total: 50 },
];

// ====================================================================
// Sub-component: ChallengeItem
// ====================================================================

const ChallengeItem = ({ challenge }) => {
    // 진행률 계산
    const progressPercentage = Math.min(100, (challenge.current / challenge.total) * 100); 
    const progressWidth = `${progressPercentage}%`;
    const isCompleted = progressPercentage >= 100; // 완료 여부

    return (
        <div className={`bg-surface-light dark:bg-surface-dark rounded-lg shadow-sm overflow-hidden transform hover:-translate-y-1 transition-transform duration-300 border ${isCompleted ? 'border-green-500' : 'border-border-light dark:border-border-dark'}`}>
            <div className="p-5">
                {/* 레벨 표시 (선택사항) */}
                <p className={`text-xs font-semibold mb-1 ${challenge.level === 'BEGINNER' ? 'text-green-500' : 'text-yellow-500'}`}>
                    {challenge.level === 'BEGINNER' ? '초급' : '중급'}
                </p>
                <p className="text-lg font-semibold text-text-light-primary dark:text-text-dark-primary">{challenge.title}</p>
                <p className="text-sm text-text-light-secondary dark:text-text-dark-secondary mt-1 mb-4">{challenge.description}</p>
                
                <div className="w-full bg-gray-200 dark:bg-gray-700 rounded-full h-2.5 mb-2 relative">
                    {/* 완료 시 배경색 변경 */}
                    <div className={`h-2.5 rounded-full ${isCompleted ? 'bg-green-500' : 'bg-primary'}`} style={{ width: progressWidth }}></div>
                    {/* 완료 시 체크 아이콘 표시 */}
                    {isCompleted && (
                        <MaterialSymbol 
                            name="check_circle" 
                            className="absolute right-0 top-1/2 transform -translate-y-1/2 text-green-500 text-lg translate-x-1" 
                            style={{ fontSize: '1.25rem' }}
                        />
                    )}
                </div>
                
                <div className="flex justify-between items-center text-sm font-medium">
                    {/* 완료 상태 텍스트 표시 */}
                    <p className={`font-semibold ${isCompleted ? 'text-green-500' : 'text-primary'}`}>
                        {isCompleted ? '도전 완료' : '도전 진행 중'}
                    </p>
                    <p className="text-text-light-secondary dark:text-text-dark-secondary">
                        {Math.min(challenge.current, challenge.total)} / {challenge.total}
                    </p>
                </div>
            </div>
        </div>
    );
};

// ====================================================================
// Main Component: ChallengePage
// ====================================================================

const ChallengePage = () => {
    // 🌟 상태 정의
    const [challenges, setChallenges] = useState([]); // 모든 도전과제 목록
    const [badge, setBadge] = useState(null); // 배지 상태 추가
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchChallenges = async () => {
            setLoading(true);
            setError(null);
            try {
                // 🌟 API 호출 및 데이터 구조 변경 반영
                const response = await getContribution(); 
                const stats = response?.stats || { commit_count: 0, pr_count: 0, issue_count: 0 };
                const fetchedBadge = response?.badge || 'NONE';

                // 🌟 로컬 도전과제 목표에 통계 데이터 매핑
                const updatedChallenges = ALL_CHALLENGES_GOALS.map(goal => {
                    let currentCount = 0;
                    switch (goal.type) {
                        case 'commit':
                            currentCount = stats.commit_count;
                            break;
                        case 'pr':
                            currentCount = stats.pr_count;
                            break;
                        case 'issue':
                            currentCount = stats.issue_count;
                            break;
                        default:
                            currentCount = 0;
                    }
                    return {
                        ...goal,
                        current: currentCount, // 현재 진행도 업데이트
                    };
                });
                
                setChallenges(updatedChallenges);
                setBadge(fetchedBadge); // 배지 상태 업데이트
                
            } catch (err) {
                console.error("도전과제 목록 조회 실패:", err);
                setError("도전과제 목록 및 통계를 불러오는 데 실패했습니다.");
            } finally {
                setLoading(false);
            }
        };
        
        fetchChallenges();
    }, []);

    // 🌟 난이도별 분류를 useMemo로 처리하여 렌더링 최적화
    const { beginnerChallenges, intermediateChallenges } = useMemo(() => {
        const beginner = challenges.filter(c => c.level === 'BEGINNER');
        const intermediate = challenges.filter(c => c.level === 'INTERMEDIATE');
        return { beginnerChallenges: beginner, intermediateChallenges: intermediate };
    }, [challenges]);


    // ----------------------------------------------------
    // 로딩 및 에러 상태 처리
    // ----------------------------------------------------
    
    if (loading) {
        return (
            <div className="min-h-screen flex items-center justify-center bg-background-light dark:bg-background-dark font-display">
                <p className="text-lg text-text-light-primary dark:text-text-dark-primary">도전과제를 불러오는 중입니다...</p>
            </div>
        );
    }

    if (error) {
        return (
            <div className="min-h-screen flex flex-col items-center justify-center bg-background-light dark:bg-background-dark font-display">
                <MaterialSymbol name="error" className="text-red-500 text-5xl mb-4" />
                <p className="text-xl text-red-500">에러 발생</p>
                <p className="text-text-light-secondary dark:text-text-dark-secondary mt-2">{error}</p>
            </div>
        );
    }

    const allChallengesEmpty = challenges.length === 0;

    // ----------------------------------------------------
    // JSX 렌더링
    // ----------------------------------------------------

    // 🌟 배지 색상 및 텍스트 매핑
    const badgeColors = {
        BRONZE: 'bg-yellow-800 text-white',
        SILVER: 'bg-gray-400 text-white',
        GOLD: 'bg-yellow-500 text-white',
        NONE: 'bg-gray-200 text-gray-500',
    };
    const badgeText = badge === 'NONE' ? '배지 없음' : `${badge} 배지`;


    return (
        
        <div className="min-h-screen bg-background-light dark:bg-background-dark font-display text-text-light-primary dark:text-text-dark-primary antialiased">
            
            <Header /> 
            
            <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                
                <h1 className="text-3xl md:text-4xl font-bold text-text-light-primary dark:text-text-dark-primary mb-2 text-center">
                    도전과제 리스트
                </h1>
                
                {/* 🌟 배지 표시 영역 */}
                <div className="text-center mb-10">
                    <span className={`inline-flex items-center px-4 py-1.5 rounded-full text-sm font-bold tracking-wider ${badgeColors[badge] || badgeColors.NONE}`}>
                        <MaterialSymbol name="workspace_premium" className="mr-2 text-lg" />
                        {badgeText}
                    </span>
                </div>

                {allChallengesEmpty && (
                    <div className="text-center py-20 bg-surface-light dark:bg-surface-dark rounded-lg shadow-md">
                        <MaterialSymbol name="sentiment_dissatisfied" className="text-gray-400 text-6xl mb-4" />
                        <p className="text-lg text-gray-500 dark:text-gray-400">현재 정의된 도전과제가 없습니다.</p>
                    </div>
                )}
                
                {/* 초급 섹션 */}
                {beginnerChallenges.length > 0 && (
                    <section className="mb-12">
                        <h2 className="text-2xl font-semibold text-text-light-primary dark:text-text-dark-primary mb-6 flex items-center gap-3">
                            <MaterialSymbol name="local_florist" className="text-green-500 text-3xl" />
                            난이도 - 초급
                        </h2>
                        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
                            {/* 🌟 업데이트된 API 데이터 맵핑 */}
                            {beginnerChallenges.map(challenge => (
                                <ChallengeItem key={challenge.id} challenge={challenge} />
                            ))}
                        </div>
                    </section>
                )}

                {/* 중급 섹션 */}
                {intermediateChallenges.length > 0 && (
                    <section>
                        <h2 className="text-2xl font-semibold text-text-light-primary dark:text-text-dark-primary mb-6 flex items-center gap-3">
                            <MaterialSymbol name="local_fire_department" className="text-yellow-500 text-3xl" />
                            난이도 - 중급
                        </h2>
                        <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
                            {/* 🌟 업데이트된 API 데이터 맵핑 */}
                            {intermediateChallenges.map(challenge => (
                                <ChallengeItem key={challenge.id} challenge={challenge} />
                            ))}
                        </div>
                    </section>
                )}

            </main>
        </div>
    );
};

export default ChallengePage;