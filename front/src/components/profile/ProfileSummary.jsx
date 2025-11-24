import React from 'react';
import MaterialSymbol from '../ui/MaterialSymbol';
import { userData } from '../../data/mypagedata'; // Mock Data import

//뱃지함수
const getBadgeRank = (status) => {
    if (status === 3) {
        return {
            name: "GOLD",
            colorClass: "text-yellow-500 border-yellow-500 bg-yellow-500/10", 
        };
    } else if(status === 2) {
        return {
            name: "SILVER",
            colorClass: "text-gray-400 border-gray-400 bg-gray-400/10", 
        };
    } else if(status === 1){
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
                    <h2 className="text-xl font-bold text-gray-900 dark:text-white">{userData.nickname}</h2>
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