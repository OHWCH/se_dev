import React from 'react';
import { useEffect, useState } from 'react';
import MaterialSymbol from '../ui/MaterialSymbol';
import { userData } from '../../data/mypagedata'; // Mock Data import
import { getContribution } from '../../services/userApi';

//뱃지함수
const getBadgeRank = (status) => {
    if (status === "GOLD") {
        return {
            name: "GOLD",
            colorClass: "text-yellow-500 border-yellow-500 bg-yellow-500/10", 
        };
    } else if(status === "SILVER") {
        return {
            name: "SILVER",
            colorClass: "text-gray-400 border-gray-400 bg-gray-400/10", 
        };
    } else if(status === "BRONZE"){
        return {
            name: "BRONZE",
            colorClass: "text-yellow-700 border-yellow-700 bg-yellow-700/10", 
        };
    } else {
        return {
            name: "UNKNOWN",
            colorClass: "text-gray-900 border-gray-900 bg-gray-900/10",
        }
    }
};

const ProfileSummary = () => {

    const [userData, setUserData] = useState({
        commits: 0,
        prs: 0,
        issues: 0,
        badgeStatus: "Unknown",
    });
    const [loading, setLoading] = useState(true);
    const [error, setError] = useState(null);

    useEffect(() => {
        const fetchUserContribution = async () => {
            setLoading(true);
            try {
                // 이전에 구현한 인증 헤더 포함된 API 호출
                const data = await getContribution(); 
                
                // 🔑 백엔드 응답 데이터(data)를 userData 상태에 저장
                setUserData({
                    commits: data.stats.commit_count || 0,
                    prs: data.stats.pr_count || 0,
                    issues: data.stats.issue_count || 0,
                    // 백엔드 키 이름이 'commits', 'prs', 'issues'와 다를 경우 여기에 매핑해줍니다.
                    badgeStatus: data.badge || "Unknown"
                });
                setError(null);
            } catch (err) {
                console.error("기여도 데이터 로드 오류:", err);
                setError("기여도 정보를 불러오는 데 실패했습니다. 로그인을 확인해주세요.");
                setUserData({ commits: 'N/A', prs: 'N/A', issues: 'N/A' });
            } finally {
                setLoading(false);
            }
        };

        fetchUserContribution();
    }, []);
    const badge = getBadgeRank(userData.badgeStatus);
    return (
        // 시안: lg:col-span-1 클래스 적용
        <div className="lg:col-span-1 bg-white dark:bg-gray-800 p-6 rounded-lg shadow-md border border-gray-200 dark:border-gray-700">
            <div className="flex items-center space-x-4 mb-6">
                {/* primary 색상 배경, 코드 아이콘 */}
                <div className="w-16 h-16 rounded-full bg-primary flex items-center justify-center">
                    <MaterialSymbol name="code" className="text-4xl text-white" />
                </div>
                <div>
                    <h2 className="text-xl font-bold text-gray-900 dark:text-white">{localStorage.getItem("currentGithubId")}</h2>
                    <p className="text-sm text-gray-500 dark:text-gray-400">닉네임</p>
                    
                </div>
                    <span className={`px-2 py-0.5 text-xs font-bold rounded-full border ${badge.colorClass}`}>
                        {badge.name}
                    </span>
            </div>
            
            <div className="space-y-4">
                {/* 데이터 표시 섹션 */}
                <div className="flex justify-between items-baseline">
                    <span className="text-gray-600 dark:text-gray-300">총 커밋 횟수</span>
                    <span className="font-bold text-lg text-primary">{userData.commits}</span>
                </div>
                <div className="flex justify-between items-baseline">
                    <span className="text-gray-600 dark:text-gray-300">총 PR 횟수</span>
                    <span className="font-bold text-lg text-primary">{userData.prs}</span>
                </div>
                <div className="flex justify-between items-baseline">
                    <span className="text-gray-600 dark:text-gray-300">총 이슈 생성 횟수</span>
                    <span className="font-bold text-lg text-primary">{userData.issues}</span>
                </div>
            </div>
        </div>
    );
};

export default ProfileSummary;