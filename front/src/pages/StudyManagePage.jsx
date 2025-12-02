import React, { useState, useEffect, useMemo } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { patchStudyForm } from '../hooks/useStudyForm';
import Header from '../components/ui/Header';
import MaterialSymbol from '../components/ui/MaterialSymbol';
import { mockCategories } from '../data/studyData';
import { Link } from 'react-router-dom';
import { getStudyMember, approveApplicant, rejectApplicant, deleteMember, createStudySchedule, getStudyMain, getStudyDetail, deleteStudy } from '../services/studyApi';

// ---------------------------------------------------------------------
// 탭 컴포넌트 1: 스터디 정보 수정 폼
// ---------------------------------------------------------------------
const mapToUpdatePayload = (detailData) => {
    // 🚨 detailData가 undefined일 경우, 이 함수 자체가 호출되지 않도록 메인 컴포넌트에서 보호합니다.
    const { studyInfo } = detailData;
    
    return {
        studyName: studyInfo.studyName,
        studyDescription: studyInfo.studyDescription,
        studyCategory: studyInfo.studyCategory,
        maxMembers: studyInfo.maxMemberCount, // 🌟 폼 필드에 맞게 문자열로 변환하여 전달
    };
};

const StudyInfoTab = ({ study, studyId }) => {
    const navigate = useNavigate();

    const initialDataForHook = useMemo(() => ({
        studyInfo: study // study prop은 부모에서 이미 안정화됨 (newPayload)
    }), [study]);

    const { 
            formData, 
            handleChange, 
            handleSubmit, 
            isSubmitting 
        } = patchStudyForm(initialDataForHook, studyId);

        const handleDeleteStudy = async () => {

            const studyData = {
            "studyName": study.studyName,
            "studyDescription": study.studyDescription,
            "studyCategory": study.studyCategory,
            "maxMembers": study.maxMemberCount, 
            };

        if (window.confirm('🚨 정말로 스터디를 삭제하시겠습니까? 삭제된 스터디는 복구할 수 없습니다.')) {
            try {
                // deleteStudy API 호출
                await deleteStudy(studyId); 
                alert(`스터디가 성공적으로 삭제되었습니다.`);
                
                // 삭제 후 메인 페이지 또는 마이 스터디 목록 페이지로 이동
                navigate('/studylist'); 
                
            } catch (error) {
                console.error("스터디 삭제 실패:", error);
                alert(`스터디 삭제에 실패했습니다. (${error.message || '서버 오류'})`);
            }
        }
    };

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
                        name="studyName" 
                        type="text"
                        value={formData.studyName}
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
                        {mockCategories.filter(c => c).map(category => ( 
                            <div key={category} className="flex items-center">
                                <input 
                                    className="h-4 w-4 rounded border-border-light dark:border-border-dark text-primary focus:ring-primary bg-background-light dark:bg-background-dark" 
                                    id={`cat-${category}`} 
                                    name="studyCategory" // OK
                                    type="radio"
                                    value={category}
                                    checked={formData.studyCategory === category} 
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
                        name="studyDescription" 
                        rows="8"
                        value={formData.studyDescription}
                        onChange={handleChange}
                    />
                </div>
            </div>

            {/* 저장 버튼 */}
            <div className="flex justify-end space-x-4 mt-8">
                <button
                    type="button" // 폼 제출을 막기 위해 type="button" 설정
                    onClick={handleDeleteStudy} // 🌟 삭제 핸들러 연결
                    className="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-red-600 hover:bg-red-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-red-500 transition-colors"
                >
                    <MaterialSymbol icon="delete" className="mr-2" />
                    스터디 삭제
                </button>
                <button
                    type="submit"
                    onClick={handleSubmit} // 기존 정보 수정 핸들러 연결
                    disabled={isSubmitting}
                    className="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-primary hover:bg-primary-dark focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary-focus transition-colors disabled:opacity-50"
                >
                    {isSubmitting ? '수정 중...' : (
                        <>
                            <MaterialSymbol icon="edit" className="mr-2" />
                            정보 수정
                        </>
                    )}
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
                const isLeader = member.studyRole === 'LEADER';
                const isApproved = member.joinStatus === 'APPROVED'
                return (
                    isApproved && (
                        <div key={index} className="flex items-center justify-between p-3 bg-gray-50 dark:bg-gray-700/50 rounded-lg border border-border-light dark:border-border-dark">
                            <div className="flex items-center space-x-3">
                                <div className="w-8 h-8 rounded-full bg-slate-200 dark:bg-slate-700 flex items-center justify-center">
                                    <MaterialSymbol name="person" className="text-slate-500 dark:text-slate-400 text-lg" style={{ fontSize: '1.25rem' }} />
                                </div>
                                <span className={`font-medium text-sm ${isLeader ? 'text-primary' : 'text-text-light-primary dark:text-text-dark-primary'}`}>
                                    {member.githubId}
                                </span>
                                {isLeader && <span className="text-xs text-primary bg-primary/10 px-2 py-0.5 rounded-full font-bold">LEADER</span>}
                            </div>
                            
                            {/* 리더가 아닐 때만 추방 버튼 표시 */}
                            {!isLeader && (
                                <button className="px-3 py-1 text-xs font-medium text-red-600 dark:text-red-500 rounded-md border border-red-500/50 hover:bg-red-50 dark:hover:bg-red-500/10 transition-colors" onClick={() => deleteMember(studyId, member.userId)}>
                                    추방
                                </button>
                            )}
                        </div>
                    )
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
                        <li key={app.userId} className="px-6 py-4">
                            <div className="flex items-center justify-between">
                                <div className="flex items-center space-x-3">
                                    <div className="flex-shrink-0 w-8 h-8 flex items-center justify-center bg-gray-200 dark:bg-zinc-700 rounded-full">
                                        <MaterialSymbol name="person" className="text-gray-500 dark:text-gray-400 text-lg" style={{ fontSize: '1.25rem' }} />
                                    </div>
                                    <p className="text-sm font-medium text-text-light-primary dark:text-text-dark-primary">{app.githubId}</p>
                                    <p className="text-xs text-text-light-secondary dark:text-text-dark-secondary ml-4">상태:{app.joinStatus}</p>
                                </div>
                                <div className="flex items-center space-x-2">
                                    {/* 수락/거절 버튼 */}
                                    <button className="px-2.5 py-1 text-xs font-semibold text-primary border border-primary rounded-md hover:bg-primary/10 transition-colors" type="button" onClick={() => approveApplicant(studyId, app.userId)}>수락</button>
                                    <button className="px-2.5 py-1 text-xs font-semibold text-text-light-secondary dark:text-text-dark-secondary border border-border-light dark:border-border-dark rounded-md hover:bg-gray-100 dark:hover:bg-gray-700 transition-colors" type="button" onClick={() => rejectApplicant(studyId, app.userId)}>거절</button>
                                </div>
                            </div>
                        </li>
                    ))}
                </ul>
            )}
        </div>
    );
};

const ScheduleCreateTab = ({ studyId }) => {
    // 폼 초기 상태: 시작 시간과 종료 시간은 현재 시간을 기준으로 초기화
    const now = new Date();
    // UTC 기준이 아닌 로컬 시간 문자열로 변환 (YYYY-MM-DDTHH:MM)
    const formatLocalDateTime = (date) => {
        const year = date.getFullYear();
        const month = String(date.getMonth() + 1).padStart(2, '0');
        const day = String(date.getDate()).padStart(2, '0');
        const hours = String(date.getHours()).padStart(2, '0');
        const minutes = String(date.getMinutes()).padStart(2, '0');
        return `${year}-${month}-${day}T${hours}:${minutes}`;
    };

    const initialScheduleData = {
        comment: '',
        startedAt: formatLocalDateTime(now),
        // 2시간 뒤로 초기 설정 (예시)
        endAt: formatLocalDateTime(new Date(now.getTime() + 2 * 60 * 60 * 1000)),
    };

    const [scheduleData, setScheduleData] = useState(initialScheduleData);
    const [isSubmitting, setIsSubmitting] = useState(false);

    const handleChange = (e) => {
        const { name, value } = e.target;
            setScheduleData(prev => ({
                ...prev,
                // 🌟 FIX: name 속성을 키로 사용하여 value를 업데이트합니다.
                [name]: value, 
            }));
    };

    const validate = (data) => {
        if (!data.comment.trim()) return '일정 코멘트를 입력해주세요.';
        if (new Date(data.startedAt) >= new Date(data.endAt)) return '종료 시간은 시작 시간보다 늦어야 합니다.';
        return null;
    };

    const handleSubmit = async (e) => {
        e.preventDefault();
        
        const validationError = validate(scheduleData);
        if (validationError) {
            alert(validationError);
            return;
        }

        setIsSubmitting(true);
        
        // 백엔드가 요구하는 ISO 8601 형식으로 변환 (초, 밀리초, Z 포함)
        const payload = {
            comment: scheduleData.comment,
            // 🚨 ISO 문자열 변환: new Date(localString).toISOString()
            startedAt: new Date(scheduleData.startedAt).toISOString(),
            endAt: new Date(scheduleData.endAt).toISOString(),
        };

        try {
            await createStudySchedule(studyId, payload);
        
            setScheduleData(initialScheduleData); 
            
        } catch (error) {
            throw error;
        } finally {
            setIsSubmitting(false);
        }
    };

    return (
        <form onSubmit={handleSubmit} className="space-y-6">
            <div>
                <label htmlFor="comment" className="block text-sm font-medium text-text-light-primary dark:text-text-dark-primary">
                    일정 제목/코멘트
                </label>
                <input
                    type="text"
                    id="comment"
                    name="comment"
                    value={scheduleData.comment}
                    onChange={handleChange}
                    required
                    className="mt-1 block w-full rounded-md border-border-light dark:border-border-dark bg-surface-light-field dark:bg-surface-dark-field text-text-light-primary dark:text-text-dark-primary shadow-sm focus:border-primary focus:ring-primary sm:text-sm"
                    placeholder="예: 백엔드 API 연동 회의"
                />
            </div>

            <div className="flex space-x-4">
                <div className="w-1/2">
                    <label htmlFor="startedAt" className="block text-sm font-medium text-text-light-primary dark:text-text-dark-primary">
                        시작 시간
                    </label>
                    <input
                        type="datetime-local"
                        id="startedAt"
                        name="startedAt"
                        value={scheduleData.startedAt}
                        onChange={handleChange}
                        required
                        className="mt-1 block w-full rounded-md border-border-light dark:border-border-dark bg-surface-light-field dark:bg-surface-dark-field text-text-light-primary dark:text-text-dark-primary shadow-sm focus:border-primary focus:ring-primary sm:text-sm"
                    />
                </div>
                <div className="w-1/2">
                    <label htmlFor="endAt" className="block text-sm font-medium text-text-light-primary dark:text-text-dark-primary">
                        종료 시간
                    </label>
                    <input
                        type="datetime-local"
                        id="endAt"
                        name="endAt"
                        value={scheduleData.endAt}
                        onChange={handleChange}
                        required
                        className="mt-1 block w-full rounded-md border-border-light dark:border-border-dark bg-surface-light-field dark:bg-surface-dark-field text-text-light-primary dark:text-text-dark-primary shadow-sm focus:border-primary focus:ring-primary sm:text-sm"
                    />
                </div>
            </div>
            

            <div className="pt-5">
                <button
                    type="submit"
                    disabled={isSubmitting}
                    className="w-full inline-flex justify-center items-center px-4 py-2 border border-transparent text-base font-medium rounded-md shadow-sm text-white bg-primary hover:bg-primary-dark focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary-focus transition-colors disabled:opacity-50"
                >
                    {isSubmitting ? '일정 생성 중...' : (
                        <>
                            <MaterialSymbol icon="schedule" className="mr-2" />
                            일정 생성하기
                        </>
                    )}
                </button>
            </div>
        </form>
    );
};

// ---------------------------------------------------------------------
// 메인 컴포넌트
// ---------------------------------------------------------------------
const StudyManagePage = () => {
    const { id } = useParams();
    const navigate = useNavigate();
    const [foundStudyDetail, setFoundStudyDetail] = useState(null);
    const [foundStudyMembers, setFoundStudyMembers] = useState(null);
    const [loading, setLoading] = useState(true); // 기존 로딩 상태
    const [loadingError, setLoadingError] = useState(false); // 🌟 상태 추가

    const tabs = [
        // 이 tabs 배열을 정의하는 코드는 데이터 로딩 상태와 관계 없이 미리 정의 가능합니다.
        // 다만, StudyInfoTab 내에서 foundStudyDetail이 null일 때의 처리가 필요할 수 있습니다.
        // 여기서는 데이터 로딩이 완료된 후에만 탭 내용을 렌더링하도록 처리하겠습니다.
    ];
    
    const [activeTab, setActiveTab] = useState("스터디 정보 수정");

    useEffect(() => {
        const fetchStudyData = async () => {
            let detailData = null; // 🌟 detailData 초기화 유지 (하위 컴포넌트 호환용)
            let memberData = null;
            let applicantData = null;
            try {
                setLoading(true);
                setLoadingError(false); // 🌟 setLoadingError 호출

                // 1. API 호출
                detailData = await getStudyMain(id); // 🌟 mainData에 할당
                memberData = await getStudyMember(id);
                applicantData = await getStudyDetail(id);
                
                // 2. 유효성 검사
                if (!detailData) {
                    throw new Error("스터디 데이터를 찾을 수 없거나 API 응답이 유효하지 않습니다.");
                }

                // 3. 리더 권한 체크 (githubId 비교)
                const leaderMember = detailData.members.find(m => m.studyRole === 'LEADER');
                const leaderGithubId = leaderMember ? leaderMember.githubId : null;

                const currentGithubId = localStorage.getItem("currentGithubId");

                if (!currentGithubId) {
                    // githubId가 없으면 인증 실패로 간주
                    throw new Error("Local Storage에 GitHub ID가 없습니다. 로그인 정보 확인 필요.");
                }

                const isCurrentUserLeader = leaderGithubId === currentGithubId;

                if (!isCurrentUserLeader) {
                    alert('스터디 리더만 스터디 관리에 접근할 수 있습니다.');
                    setLoading(false); // 로딩 종료
                    navigate(`/study/${id}`, { replace: true });
                    return; 
                }

                const formattedDetail = {
                    studyInfo: {
                        studyId: detailData.studyId,
                        studyName: detailData.studyName,
                        studyDescription: detailData.studyDescription,
                        studyCategory: detailData.studyCategory,
                        maxMemberCount: detailData.maxMembers,
                    },
                    // 🌟 getStudyMain에서 가져온 members를 사용
                    members: detailData.members, 
                    // getStudyMember 응답에서 applicants를 가져와야 합니다.
                    applicants: applicantData.applicants || [], 
                };

                setFoundStudyDetail(formattedDetail);
                setFoundStudyMembers(formattedDetail.members); // 🌟 구성원 목록은 mainData 기반으로 업데이트
                setLoading(false); // 🌟 성공 시 로딩 종료
                
            } catch (error) {
                console.error("API 호출 중 오류 발생:", error);
                
                const errorMessage = error.message.includes("GitHub ID") 
                                   ? "로그인 정보가 유효하지 않습니다. 다시 로그인해주세요." 
                                   : "스터디 정보를 불러오는데 실패했습니다.";
                                   
                // 🌟 에러 발생 시 로딩 및 에러 상태 설정
                setLoading(false);
                setLoadingError(true);
                alert(errorMessage);
                navigate(`/studylist`, { replace: true }); 
            }
        };
        
        fetchStudyData();
    }, [id, navigate]);
    

    const newPayload = useMemo(() => {
        // 이미 로딩 체크가 if문으로 보호하고 있지만, useMemo 내에서도 안전하게 처리
        if (!foundStudyDetail || !foundStudyDetail.studyInfo) {
            return null;
        }
        return mapToUpdatePayload(foundStudyDetail);
    }, [foundStudyDetail]);


    const actualTabs = useMemo(() => {
        if (!newPayload || !foundStudyMembers) {
            return []; // 데이터 로딩 중일 경우 빈 배열 반환
        }
        return [
            { name: '스터디 정보 수정', component: <StudyInfoTab study={newPayload} studyId = {id} /> },
            { name: '구성원 관리', component: <MemberManageTab members={foundStudyMembers} studyId={id} /> },
            { name: '참여 신청 관리', component: <ApplicationManageTab applications={foundStudyDetail.applicants} studyId={id}/> },
            
            // 🌟 새 탭 추가
            { name: '스터디 일정 생성', component: <ScheduleCreateTab studyId={id} /> }, 
        ];
    }, [newPayload, foundStudyMembers, id]);

    if (!foundStudyDetail || !foundStudyDetail.studyInfo || !foundStudyMembers) { 
        // ... (로딩 화면 렌더링) ...
        return (
            <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                <div className="text-xl font-bold tracking-tight text-text-light-primary dark:text-text-dark-primary">
                    스터디 정보를 불러오는 중입니다...
                </div>
            </main>
        );
    }
    

    return (
        <div className="min-h-screen bg-background-light dark:bg-background-dark font-display antialiased">
            <Header /> 
            
            <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                {/* 헤더 영역 */}
                <div className="flex justify-between items-center mb-6">
                    <h1 className="text-3xl font-bold tracking-tight text-text-light-primary dark:text-text-dark-primary">
                        {foundStudyDetail.studyInfo.studyName} 관리
                    </h1>
                    <Link to={`/study/${id}`} className="text-sm font-medium text-primary hover:text-primary/80 transition-colors">
                        스터디 메인으로 돌아가기 &rarr;
                    </Link>
                </div>
                
                {/* 탭 구조 */}
                <div className="max-w-4xl mx-auto">
                    
                    {/* 탭 네비게이션 */}
                    <nav className="flex space-x-4 border-b border-border-light dark:border-border-dark mb-6">
                        {actualTabs.map((tab) => (
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
                        {actualTabs.find(tab => tab.name === activeTab)?.component}
                    </div>
                </div>
            </main>
        </div>
    );
};

export default StudyManagePage;