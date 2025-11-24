import React from 'react';
import Header from '../components/ui/Header';
import MaterialSymbol from '../components/ui/MaterialSymbol';

// ====================================================================
// Mock Data (실제로는 API 등에서 가져와야 합니다)
// ====================================================================

const mockBeginnerChallenges = [
    { id: 'b1', title: '커밋 1회 완료', description: '첫 번째 커밋을 성공적으로 완료하세요.', current: 0, total: 1 },
    { id: 'b2', title: '커밋 5회 완료', description: '총 5개의 커밋을 완료하세요.', current: 0, total: 5 },
    { id: 'b3', title: '커밋 10회 완료', description: '총 10개의 커밋을 완료하세요.', current: 0, total: 10 },
];

const mockIntermediateChallenges = [
    { id: 'i1', title: 'PR 1회 완료', description: '첫 번째 Pull Request를 생성하고 머지하세요.', current: 0, total: 1 },
    { id: 'i2', title: 'PR 5회 완료', description: '총 5개의 Pull Request를 완료하세요.', current: 0, total: 5 },
    { id: 'i3', title: 'PR 10회 완료', description: '총 10개의 Pull Request를 완료하세요.', current: 0, total: 10 },
    { id: 'i4', title: '이슈 1회 등록', description: '프로젝트에 첫 번째 이슈를 등록하세요.', current: 0, total: 1 },
    { id: 'i5', title: '이슈 5회 등록', description: '총 5개의 이슈를 등록하세요.', current: 0, total: 5 },
    { id: 'i6', title: '이슈 10회 등록', description: '총 10개의 이슈를 등록하세요.', current: 0, total: 10 },
];

// ====================================================================
// Sub-component: ChallengeItem
// ====================================================================

const ChallengeItem = ({ challenge }) => {
    // 진행률 계산
    const progressPercentage = (challenge.current / challenge.total) * 100;
    const progressWidth = `${progressPercentage}%`;


    return (
        <div className="bg-surface-light dark:bg-surface-dark rounded-lg shadow-sm border border-border-light dark:border-border-dark overflow-hidden transform hover:-translate-y-1 transition-transform duration-300">
            <div className="p-5">
                <p className="text-lg font-semibold text-text-light-primary dark:text-text-dark-primary">{challenge.title}</p>
                <p className="text-sm text-text-light-secondary dark:text-text-dark-secondary mt-1 mb-4">{challenge.description}</p>
                
            
                <div className="w-full bg-gray-200 dark:bg-gray-700 rounded-full h-2.5 mb-2">
                    <div className="bg-primary h-2.5 rounded-full" style={{ width: progressWidth }}></div>
                </div>
                
                <p className="text-right text-sm font-medium text-text-light-secondary dark:text-text-dark-secondary">
                    {challenge.current} / {challenge.total}
                </p>
            </div>
        </div>
    );
};

// ====================================================================
// Main Component: ChallengePage
// ====================================================================

const ChallengePage = () => {
    return (
        
        <div className="min-h-screen bg-background-light dark:bg-background-dark font-display text-text-light-primary dark:text-text-dark-primary antialiased">
            
            
            <Header /> 
            
           
            <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                
                <h1 className="text-3xl md:text-4xl font-bold text-text-light-primary dark:text-text-dark-primary mb-8 md:mb-12 text-center">
                    도전과제 리스트
                </h1>

                {/* 초급 섹션 */}
                <section className="mb-12">
                    <h2 className="text-2xl font-semibold text-text-light-primary dark:text-text-dark-primary mb-6 flex items-center gap-3">
                        <MaterialSymbol name="local_florist" className="text-green-500 text-3xl" />
                        난이도 - 초급
                    </h2>
                    <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
                        {mockBeginnerChallenges.map(challenge => (
                            <ChallengeItem key={challenge.id} challenge={challenge} />
                        ))}
                    </div>
                </section>

                {/* 중급 섹션 */}
                <section>
                    <h2 className="text-2xl font-semibold text-text-light-primary dark:text-text-dark-primary mb-6 flex items-center gap-3">
                        <MaterialSymbol name="local_fire_department" className="text-yellow-500 text-3xl" />
                        난이도 - 중급
                    </h2>
                    <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-3 gap-6">
                        {mockIntermediateChallenges.map(challenge => (
                            <ChallengeItem key={challenge.id} challenge={challenge} />
                        ))}
                    </div>
                </section>

            </main>
        </div>
    );
};

export default ChallengePage;