import React, { useState, useEffect } from 'react';
import Header from '../components/ui/Header';
import MaterialSymbol from '../components/ui/MaterialSymbol';
import StudyListItem from '../components/study/StudyListItem';
import { getStudyList } from '../services/studyApi.js';
import Pagination from '../components/ui/Pagination';
import { mockCategories } from '../data/studyData.jsx';
import { Link } from 'react-router-dom';

const StudyListPage = () => {
    const [studies, setStudies] = useState([]); // 데이터를 저장할 배열
    const [loading, setLoading] = useState(true); // 로딩 상태
    const [error, setError] = useState(null); // 에러 메시지
   
    const [activeCategory, setActiveCategory] = useState('전체');
    const mockPaginationLinks = [
    { label: '1', href: '#', current: true },
    { label: '2', href: '#', current: false },
    { label: '3', href: '#', current: false },
    { label: '...', href: '#', current: false, disabled: true },
    { label: '10', href: '#', current: false },
    ];

    useEffect(() => {
        const fetchStudies = async () => {
            setLoading(true); // 로딩 시작
            try {
                const fetchedStudies = await getStudyList(); 
                
                setStudies(fetchedStudies); 
                
                setError(null);
            } catch (err) {
                console.error("데이터 패칭 오류:", err);
                setError("스터디 목록을 불러오는 데 실패했습니다."); 
                setStudies([]);
            } finally {
                setLoading(false); // 로딩 종료
            }
        };
        
        fetchStudies();
    }, []); // 훅이 마운트될 때 한 번만 실행


    return (
        <div className="min-h-screen bg-background-light dark:bg-background-dark font-display text-text-light-primary dark:text-text-dark-primary antialiased">
            <Header /> 
            
            <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                {/* 상단 제목 및 버튼 영역 */}
                <div className="flex flex-col lg:flex-row justify-between items-start lg:items-center mb-8 gap-4">
                    <h1 className="text-3xl font-bold tracking-tight">스터디 목록</h1>

                    <div className='flex space-x-3'> 
                        {/* 내 스터디 버튼 */}
                        <Link 
                            to="/mystudy" // 내 스터디 페이지 경로를 /study/my 로 가정
                            className="flex items-center justify-center bg-primary text-white font-medium px-4 py-2 rounded-md text-sm hover:opacity-90 transition-opacity whitespace-nowrap"
                        >
                            <MaterialSymbol name="person_pin" className="mr-2 text-base" />
                            내 스터디
                        </Link>
                        
                        {/* 스터디 생성 버튼 (기존 primary 색상 유지) */}
                        <Link 
                            to="/study/create" 
                            className="flex items-center justify-center bg-primary text-white font-medium px-4 py-2 rounded-md text-sm hover:opacity-90 transition-opacity whitespace-nowrap"
                        >
                            <MaterialSymbol name="group_add" className="mr-2 text-base" />
                            스터디 생성
                        </Link>
                    </div>
                </div>
                
                {/* 검색 바 및 필터 */}
                <div className="bg-surface-light dark:bg-surface-dark p-6 rounded-lg shadow-sm mb-8 border border-border-light dark:border-border-dark">
                    <div className="relative mb-4">
                        <input
                            type="text"
                            placeholder="스터디 이름, 태그 등으로 검색"
                            className="w-full pl-10 pr-4 py-2 border border-border-light dark:border-border-dark rounded-lg bg-background-light dark:bg-background-dark focus:ring-primary focus:border-primary text-text-light-primary dark:text-text-dark-primary placeholder-text-light-secondary"
                        />
                        <MaterialSymbol name="search" className="absolute left-3 top-1/2 transform -translate-y-1/2 text-text-light-secondary" />
                    </div>
                    
                    {/* 카테고리 필터링 탭 */}
                    <div className="flex flex-wrap gap-2 text-sm">
                        {mockCategories.map(category => (
                            <button
                                key={category}
                                onClick={() => setActiveCategory(category)}
                                className={`px-3 py-1.5 rounded-full font-medium transition-colors ${
                                    activeCategory === category
                                        ? 'bg-primary text-white shadow-sm'
                                        : 'bg-background-light dark:bg-background-dark text-text-light-secondary dark:text-text-dark-secondary hover:bg-gray-100 dark:hover:bg-gray-700 border border-border-light dark:border-border-dark'
                                }`}
                            >
                                {category}
                            </button>
                        ))}
                    </div>
                </div>

                {/* 스터디 목록 (3열 그리드) */}
                <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                    {studies.map(study => (
                        <StudyListItem key={study.studyId} study={study} /> 
                    ))}
                </div>
                
                {/* 페이지네이션 (재사용) */}
                <div className="max-w-4xl mx-auto">
                {/* 🌟 links prop을 전달합니다. */}
                    <Pagination links={mockPaginationLinks} /> 
                </div>
                
            </main>
        </div>
    );
};

export default StudyListPage;