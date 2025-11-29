import React, { useState, useEffect } from 'react'; // 🌟 useState, useEffect 임포트 추가
import { useParams } from 'react-router-dom';
import Header from '../components/ui/Header';
import MaterialSymbol from '../components/ui/MaterialSymbol';
// mockStudyDetail는 더 이상 사용하지 않음
import { Link } from 'react-router-dom';
import { getStudyDetail, getStudyMember, getStudySchedule } from '../services/studyApi'; // API 함수는 비동기 함수로 가정


const formatTime = (isoString) => {
    if (!isoString) return '';
    // T를 기준으로 날짜와 시간 분리
    const [datePart, timePart] = isoString.split('T');
    const timeOnly = timePart ? timePart.substring(0, 5) : ''; // HH:MM 형식
    
    // YYYY-MM-DDT... 형식 대신 MM/DD HH:MM 형식으로 표시
    const [year, month, day] = datePart.split('-');
    return `${month}/${day} ${timeOnly}`;
};

const TaskItem = ({ task, studyId }) => {
    // 🌟 로컬 상태 관리: 참여 여부와 참여 인원
    const [isParticipated, setIsParticipated] = useState(task.participated);
    const [currentCount, setCurrentCount] = useState(task.participateCount);
    const [isToggling, setIsToggling] = useState(false);
    
    // task prop이 변경될 때마다 로컬 상태 동기화 (재로드 시)
    useEffect(() => {
        setIsParticipated(task.participated);
        setCurrentCount(task.participateCount);
    }, [task.participated, task.participateCount]);
    
    // 🌟 참가/취소 토글 핸들러
    const handleParticipationToggle = async () => {
        if (isToggling) return;
        setIsToggling(true);
        
        try {
            if (isParticipated) {
                // 참가 취소
                await cancelSchedule(studyId, task.scheduleId); // 🚨 API 호출
                setIsParticipated(false);
                setCurrentCount(prev => prev - 1);
                alert(`'${task.comment}' 일정 참가를 취소했습니다.`);
            } else {
                // 참가
                await joinSchedule(studyId, task.scheduleId); // 🚨 API 호출
                setIsParticipated(true);
                setCurrentCount(prev => prev + 1);
                alert(`'${task.comment}' 일정에 참가했습니다.`);
            }
            
        } catch (error) {
            console.error("일정 참여 토글 실패:", error);
            alert(error.message || "일정 참여 상태 변경에 실패했습니다.");
        } finally {
            setIsToggling(false);
        }
    };
    
    const startTimeFormatted = formatTime(task.startedAt);
    const endTimeFormatted = formatTime(task.endAt);
    
    return (
        <div className="flex items-center justify-between py-3 border-b border-border-light dark:border-border-dark last:border-b-0">
            <div className="flex-1 min-w-0 pr-4">
                {/* 일정 제목 */}
                <p className="text-sm font-medium text-text-light-primary dark:text-text-dark-primary truncate">
                    {task.comment}
                </p>
                {/* 시간 정보 */}
                <p className="text-xs text-text-light-secondary dark:text-text-dark-secondary mt-1">
                    {startTimeFormatted} ~ {endTimeFormatted}
                </p>
            </div>
            
            <div className="flex items-center space-x-4">
                {/* 인원 수 */}
                <div className="flex items-center text-sm font-medium">
                    <MaterialSymbol name="group" className="mr-1 text-base text-text-light-secondary dark:text-text-dark-secondary" />
                    <span className="text-text-light-secondary dark:text-text-dark-secondary">{currentCount}</span>
                    <span className="mx-0.5 text-text-light-secondary dark:text-text-dark-secondary">/</span>
                    <span className="text-text-light-secondary dark:text-text-dark-secondary">{task.totalMemberCount}</span>
                </div>
                
                {/* 참가/취소 버튼 */}
                <button 
                    onClick={handleParticipationToggle}
                    disabled={isToggling}
                    className={`px-3 py-1 text-xs font-semibold rounded-md transition-colors whitespace-nowrap ${
                        isParticipated
                            ? 'bg-red-500 text-white hover:bg-red-600' // 취소 버튼 (참가 상태)
                            : 'bg-primary text-white hover:bg-primary-dark' // 참가 버튼 (미참가 상태)
                    }`}
                >
                    {isToggling ? '처리 중' : (isParticipated ? '참가 취소' : '참가')}
                </button>
            </div>
        </div>
    );
};

const TaskList = ({ tasks, studyId }) => {
    if (!tasks || tasks.length === 0) {
        return (
            <div className="py-6 text-center text-sm text-text-light-secondary dark:text-text-dark-secondary">
                지금은 일정이 없습니다.
            </div>
        );
    }

    return (
        <div>
            {tasks.map((task) => (
                // 🚨 TaskItem에 studyId 전달
                <TaskItem key={task.scheduleId} task={task} studyId={studyId} />
            ))}
        </div>
    );
};

const MemberItem = ({ member }) => {
    // 🌟 백엔드 데이터에 맞춰 'studyRole'을 'LEADER'와 'MEMBER'로 가정하고 처리
    const isLeader = member.studyRole === 'LEADER';
    
    // 신청자(APPLIED)는 아직 멤버 목록에 포함되지 않았다고 가정하고, APPROVED만 보여줍니다.
    if (member.joinStatus !== 'APPROVED') return null; 

    return (
        <div className="flex items-center space-x-3 py-2 border-b border-border-light dark:border-border-dark last:border-b-0">
            <div className="w-8 h-8 rounded-full bg-slate-200 dark:bg-slate-700 flex items-center justify-center">
                <MaterialSymbol name="person" className="text-slate-500 dark:text-slate-400 text-lg" style={{ fontSize: '1.25rem' }} />
            </div>
            <span className={`font-medium text-sm ${isLeader ? 'text-primary' : 'text-text-light-primary dark:text-text-dark-primary'}`}>
                {member.nickname} {/* 🌟 nickname 사용 */}
            </span>
            {isLeader && <span className="text-xs text-primary bg-primary/10 px-2 py-0.5 rounded-full font-bold">LEADER</span>}
        </div>
    );
};


const StudyDetailPage = () => {
    const { id } = useParams(); // 라우팅 파라미터에서 ID를 가져옵니다.

    // 🌟 상태 정의
    const [studyData, setStudyData] = useState(null);
    const [membersData, setMembersData] = useState([]);
    const [studyScheduleData, setStudyScheduleData] = useState([]);
    const [isLoading, setIsLoading] = useState(true);
    const [error, setError] = useState(null);
    
    // 🌟 데이터 패칭 및 가공 로직
    useEffect(() => {
        const fetchStudyData = async () => {
            setIsLoading(true);
            setError(null);
            
            try {
                // 1. API 호출 (비동기 처리)
                const detailResponse = await getStudyDetail(id);
                const membersResponse = await getStudyMember(id);
                const scheduleResponse = await getStudySchedule(id);

                if (!detailResponse || !detailResponse.studyInfo) {
                    setError("스터디 상세 정보를 불러오지 못했습니다.");
                    setIsLoading(false);
                    return;
                }
                
                // 2. 데이터 가공 (스터디 정보)
                const studyInfo = detailResponse.studyInfo;
                const approvedMembers = membersResponse.filter(m => m.joinStatus === "APPROVED");
                const leader = approvedMembers.find(m => m.studyRole === "LEADER");

                const processedStudy = {
                    id: studyInfo.studyId,
                    title: studyInfo.studyName,
                    category: studyInfo.studyCategory,
                    description: studyInfo.studyDescription,
                    maxMembers: studyInfo.maxMemberCount,
                    // 리더 정보 추출
                    leaderNickname: leader ? leader.nickname : '미정',
                    currentMembers: approvedMembers.length,
                    // 🌟 임시 데이터: 백엔드에서 주지 않는다면 crashing 방지용
                    upcomingTasks: scheduleResponse
                };

                setStudyData(processedStudy);
                setMembersData(membersResponse); // 멤버 데이터는 joinStatus와 role을 가진 원본을 저장
                
            } catch (err) {
                console.error("API 호출 오류:", err);
                setError("데이터를 불러오는 중 오류가 발생했습니다.");
            } finally {
                setIsLoading(false);
            }
        };

        fetchStudyData();
    }, [id]);

    if (isLoading) {
        return (
            <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8 text-center text-text-light-secondary dark:text-text-dark-secondary">
                <Header />
                <p className="py-20">스터디 정보를 불러오는 중...</p>
            </main>
        );
    }
    
    if (error || !studyData) {
        return (
            <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                <Header />
                <h1 className="text-3xl font-bold tracking-tight text-red-500">
                    오류: {error || `스터디 ID ${id}를 찾을 수 없습니다.`}
                </h1>
            </main>
        );
    }

    const study = studyData; // JSX에서 사용하기 쉽게 별칭 지정
    
    // ----------------------------------------------------
    // 🌟 JSX 렌더링
    // ----------------------------------------------------
    
    return (
        <div className="min-h-screen bg-background-light dark:bg-background-dark font-display antialiased">
            <Header /> 
            
            <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                {/* 1. 스터디 헤더 영역 */}
                <div className="flex justify-between items-center mb-6 pb-4 border-b border-border-light dark:border-border-dark">
                    {/* 🌟 studyData.title 사용 */}
                    <h1 className="text-3xl font-bold tracking-tight text-text-light-primary dark:text-text-dark-primary">{study.title}</h1>
                    
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
                            {/* 🌟 studyData.category 사용 */}
                            <p className="text-sm font-medium text-primary mb-2">{study.category}</p>
                            {/* 🌟 studyData.description 사용 */}
                            <p className="text-text-light-secondary dark:text-text-dark-secondary text-sm leading-relaxed">{study.description}</p>
                            <div className="mt-4 pt-4 border-t border-border-light dark:border-border-dark text-sm">
                                <p className="flex items-center text-text-light-secondary dark:text-text-dark-secondary">
                                    <MaterialSymbol name="group" className="mr-2 text-base" />
                                    인원: {study.currentMembers}/{study.maxMembers}
                                </p>
                                <p className="flex items-center text-text-light-secondary dark:text-text-dark-secondary mt-1">
                                    <MaterialSymbol name="star" className="mr-2 text-base" />
                                    리더: {study.leaderNickname} {/* 🌟 studyData.leaderNickname 사용 */}
                                </p>
                            </div>
                        </div>

                        {/* 구성원 카드 */}
                        <div className="bg-surface-light dark:bg-surface-dark p-2 rounded-lg shadow-sm border border-border-light dark:border-border-dark">
                            {/* 🌟 membersData.length 사용 */}
                           <h2 className="text-xl font-bold mb-4 text-text-light-primary dark:text-text-dark-primary">구성원 ({study.currentMembers}명)</h2> 
                            <div className="space-y-1">
                                {/* 🌟 membersData 맵핑 */}
                                {membersData.map((member, index) => (
                                    <MemberItem key={member.userId} member={member} />
                                ))}
                            </div>
                        </div>
                    </div>
                    
                    {/* 우측: 진행 현황 및 구성원 (2/3) */}
                    <div className="lg:col-span-2 space-y-8">
                        
                        {/* 2. 다가오는 할 일 카드 */}
                        <div className="bg-surface-light dark:bg-surface-dark p-6 rounded-lg shadow-md border border-border-light dark:border-border-dark h-full">
                            <h2 className="text-xl font-bold mb-4 text-text-light-primary dark:text-text-dark-primary">일정</h2>
                            <div className="divide-y divide-border-light dark:divide-border-dark">
                                {/* 🚨 study.id를 studyId로 전달 */}
                                <TaskList tasks={study.upcomingTasks} studyId={study.id} />
                            </div>
                        </div>
                    </div>
                </div>
            </main>
        </div>
    );
};

export default StudyDetailPage;