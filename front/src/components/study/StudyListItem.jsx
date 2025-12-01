import React from 'react';
import MaterialSymbol from '../ui/MaterialSymbol';
import { Link } from 'react-router-dom';
import { useNavigate } from 'react-router-dom';
import { joinStudy } from '../../services/studyApi';

const StudyListItem = ({ study }) => {
    //const isClosed = study.userJoinStatus === '';

    const navigate = useNavigate();
    const joinStatus = study.userJoinStatus; // APPROVED, REJECTED, APPLIED, LEFT, null
    const isClosed = study.status === 'closed';

    let buttonClass;
    let statusText;
    let isDisabled = false;
    if (isClosed) {
        // 1. 스터디 마감 상태가 최우선
        buttonClass = "bg-gray-200 dark:bg-gray-700 text-gray-500 cursor-not-allowed";
        statusText = "마감";
        isDisabled = true;
    } else {
        switch (joinStatus) {
            case 'APPROVED':
                // 2. 승인됨 (참여 중) - 초록색
                buttonClass = "bg-green-600 text-white cursor-not-allowed";
                statusText = "참여 중";
                isDisabled = true;
                break;
            case 'REJECTED':
                // 3. 거절됨 - 빨간색
                buttonClass = "bg-red-600 text-white cursor-not-allowed";
                statusText = "거절됨";
                isDisabled = true;
                break;
            case 'APPLIED':
                // 4. 승인 대기중 - 노란색 (Tailwind yellow-500 사용)
                buttonClass = "bg-yellow-500 text-gray-800 cursor-not-allowed";
                statusText = "승인 대기중";
                isDisabled = true;
                break;
            case 'LEFT':
            case null:
            default:
                // 5. 기본 상태 (LEFT, null 등) - 참여 신청 (클릭 가능)
                buttonClass = "bg-primary text-white hover:opacity-90";
                statusText = "참여 신청";
                isDisabled = false;
                break;
        }
    }

    const handleJoinClick = async (e) => {
        e.preventDefault();

        // 버튼이 비활성화 상태면 아무것도 하지 않음
        if (isDisabled) return; 

        try {
            await joinStudy(study.studyId);   // API 호출 (참여 신청)
            // 성공 후 목록 페이지로 리다이렉트
            navigate('/studylist');        
        } catch (error) {
            console.error("스터디 가입 실패:", error);
            alert("스터디 가입 신청에 실패했습니다.");
        }
    };

    return (
        <div className="bg-surface-light dark:bg-surface-dark p-6 rounded-lg shadow-sm border border-border-light dark:border-border-dark flex flex-col justify-between transition-shadow hover:shadow-md">
            <div className="flex-grow">
                <Link 
                    to={`/study/${study.studyId}`} 
                    className="flex justify-between items-start mb-3 cursor-pointer hover:opacity-80 transition-opacity" 
                >
                    <h3 className="text-lg font-bold text-text-light-primary dark:text-text-dark-primary">{study.name}</h3>
                </Link>

                <p className="text-text-light-secondary dark:text-text-dark-secondary text-sm mb-4 line-clamp-2">{study.description}</p>
                
            </div>

            <div className="pt-4 border-t border-border-light dark:border-border-dark flex items-center justify-between">
                {/* 인원수 표시 */}
                <div className="flex items-center text-text-light-secondary dark:text-text-dark-secondary text-sm">
                    <MaterialSymbol name="group" className="mr-1 text-base" style={{ fontSize: '1rem' }} />
                    {study.currentMembers} / {study.maxMembers} 명
                </div>
                
                {/* 참여/마감 버튼 */}
                <button
                    onClick={handleJoinClick} // 🌟 새로운 핸들러 사용
                    className={`px-4 py-2 text-sm font-semibold rounded-md transition-colors ${buttonClass}`}
                    disabled={isDisabled} // 🌟 isDisabled 상태 사용
                >
                    {statusText}
                </button>
            </div>
        </div>
    );
};

export default StudyListItem;