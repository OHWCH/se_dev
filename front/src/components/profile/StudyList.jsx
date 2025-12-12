import React from 'react';
import { useState, useEffect } from 'react';
import MaterialSymbol from '../ui/MaterialSymbol';
import { studyList } from '../../data/mypagedata'; // Mock Data import
import { getMyStudy } from '../../services/studyApi';


const MyStudiesList = () => {
    // 🌟 1. 스터디 목록을 상태로 관리 (초기값: 빈 배열 [])
    const [myStudies, setMyStudies] = useState([]);
    const [loading, setLoading] = useState(true);

    // 🌟 2. useEffect 내부에서 비동기 함수 호출
    useEffect(() => {
        const fetchMyStudies = async () => {
            try {
                // 🚨 FIX: getMyStudy() 호출 시 await 사용
                const data = await getMyStudy(); 
                
                // API 응답이 배열이 아닌 객체 { studies: [...] } 형태일 수 있으므로 배열인지 확인
                const studyList = Array.isArray(data) ? data : data.studies || []; 
                
                setMyStudies(studyList);
                setLoading(false);
            } catch (error) {
                console.error("내 스터디 목록 로드 실패:", error);
                setLoading(false);
                // 오류 발생 시 빈 배열로 설정하거나 오류 상태를 표시
                setMyStudies([]); 
            }
        };

        fetchMyStudies();
    }, []); // 컴포넌트 마운트 시 한 번만 실행

    if (loading) {
        return <p>내 스터디 목록을 불러오는 중...</p>;
    }
    
    // 3. 데이터가 없는 경우 처리
    if (myStudies.length === 0) {
        return (
             <div className="mt-8 p-6 rounded-lg ...">
                 <h3 className="text-lg font-semibold ...">내 스터디 목록</h3>
                 <p className="text-center text-gray-500 dark:text-gray-400 mt-4">참여 중인 스터디가 없습니다.</p>
             </div>
        );
    }
    
    return (
        // ... (원래 JSX 구조) ...
        <div className="mt-8 bg-white dark:bg-gray-800 p-6 rounded-lg shadow-md border border-gray-200 dark:border-gray-700">
            <h3 className="text-lg font-semibold text-gray-900 dark:text-white mb-4">내 스터디 목록</h3>
            <div className="space-y-4">
                {/* 🚨 FIX: 이제 myStudies는 배열이므로 .map() 사용 가능 */}
                {myStudies.map((study, index) => (
                    <div key={study.studyId} className="flex items-center justify-between p-4 bg-gray-50 dark:bg-gray-700/50 rounded-md">
                        {/* ... (나머지 렌더링 로직 유지) ... */}
                        <div>
                            {/* 🚨 필드 이름 확인: API 응답에 study.name, study.description이 있는지 확인 필요 */}
                            <p className="font-semibold text-gray-800 dark:text-gray-100">{study.name}</p>
                            <p className="text-sm text-gray-500 dark:text-gray-400">{study.description}</p>
                        </div>
                        <a 
                            className="inline-flex items-center px-4 py-2 border border-transparent text-sm font-medium rounded-md shadow-sm text-white bg-primary hover:bg-indigo-700 focus:outline-none focus:ring-2 focus:ring-offset-2 focus:ring-primary transition-colors" 
                            href={`/study/${study.studyId}`} // 🚨 study.link 대신 study.studyId를 사용하여 링크 생성
                        >
                            바로가기
                            <MaterialSymbol name="arrow_forward" className="text-lg ml-1" />
                        </a>
                    </div>
                ))}
            </div>
        </div>
    );
};

export default MyStudiesList;