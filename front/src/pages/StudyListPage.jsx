import React, { useState, useEffect, useCallback, useMemo } from 'react';
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
    const [currentPage, setCurrentPage] = useState(0); // 현재 페이지 (0부터 시작)
    const [hasNextPage, setHasNextPage] = useState(false);
    const [activeCategory, setActiveCategory] = useState('전체');

    const PAGE_SIZE = 6;

    const generatePaginationLinks = useCallback((current) => {
        // ... (페이지네이션 링크 생성 로직은 동일하게 유지)
        const links = [];
        const totalLoadedPages = current + 1; 
        const maxPagesToShow = 5;
        
        let startPage = Math.max(0, current - Math.floor(maxPagesToShow / 2));
        let endPage = Math.min(totalLoadedPages - 1, startPage + maxPagesToShow - 1);

        if (endPage - startPage < maxPagesToShow - 1) {
            startPage = Math.max(0, endPage - maxPagesToShow + 1);
        }

        if (startPage > 0) {
            links.push({ label: '1', onClick: () => handlePageChange(0), current: false });
            if (startPage > 1) {
                links.push({ label: '...', disabled: true });
            }
        }
        
        for (let i = startPage; i <= endPage; i++) {
            links.push({ 
                label: String(i + 1), 
                onClick: () => handlePageChange(i), 
                current: i === current 
            });
        }
        
        return links;
    }, []);

    const fetchStudies = useCallback(async (page) => {
        setLoading(true); 
        try {
            const response = await getStudyList(page); 

            if (page > 0 && response && Array.isArray(response) && response.length === 0) {
                 alert("마지막 페이지입니다.");
                 return; 
            }
            
            const nextExists = response && Array.isArray(response) && response.length === PAGE_SIZE;
            
            console.log(`현재 페이지: ${page}, 받은 아이템 수: ${response.length}, 다음 페이지 존재: ${nextExists}`);

            setStudies(response || []); 
            // 업데이트
            setHasNextPage(nextExists); 
            setCurrentPage(page);
            setError(null);

        } catch (err) {
            console.error("스터디 목록 조회 실패:", err);
            setError("스터디 목록을 불러오는 데 실패했습니다.");
            setStudies([]);
            setHasNextPage(false); // 오류 발생 시 다음 페이지는 없다고 가정
        } finally {
            setLoading(false); 
        }
    }, [PAGE_SIZE]); //

    const handlePageChange = useCallback((page) => {
        // 이전 페이지로의 이동(page < currentPage)은 항상 가능
        // 다음 페이지로의 이동(page === currentPage + 1)은 hasNextPage가 true일 때만 허용
        if (page >= 0 && (page < currentPage || (page === currentPage + 1 && hasNextPage))) {
            fetchStudies(page);
        }
    }, [currentPage, hasNextPage, fetchStudies]);

    useEffect(() => {
        fetchStudies(0); // 컴포넌트 마운트 시 첫 페이지(0) 로드
    }, [fetchStudies]);

    const paginationLinks = useMemo(() => {
        return generatePaginationLinks(currentPage);
    }, [currentPage, generatePaginationLinks]);


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
                            to="/mypage" // 내 스터디 페이지 경로를 /study/my 로 가정
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
                
                {!loading && studies.length === 0 && !error && (
                    <div className="text-center py-10">
                        <p className="text-lg text-gray-500 dark:text-gray-400">조회된 스터디가 없습니다.</p>
                    </div>
                )}

                {/* 🌟 페이지네이션 */}
                {studies.length > 0 && ( 
                    <div className="max-w-4xl mx-auto mt-10">
                        <Pagination 
                            links={paginationLinks} 
                            currentPage={currentPage}
                            // 🚨 수정: totalPages 대신 hasNextPage 전달
                            hasNextPage={hasNextPage} 
                            onPageChange={handlePageChange}
                        /> 
                    </div>
                )}
                
            </main>
        </div>
    );
};

export default StudyListPage;