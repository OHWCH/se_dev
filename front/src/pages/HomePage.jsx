import { React, useState, useCallback, useEffect } from 'react';
import Header from '../components/ui/Header';
import SearchBar from '../components/ui/SearchBar';
import IssueList from '../components/community/IssueList';
import { getGoodFirstIssues } from '../services/issueApi';
import Pagination from '../components/ui/Pagination';

const Homepage = () => {
    // 🌟 1. 이슈 목록 상태
    const [issues, setIssues] = useState([]);
    // 🌟 2. 로딩 상태
    const [loading, setLoading] = useState(true);
    // 🌟 3. 검색어 상태 (SearchBar와 공유)
    const [keyword, setKeyword] = useState('');
    // 🌟 4. 에러 상태
    const [error, setError] = useState(null);
    // 🌟 5. 페이지 상태 추가 (API가 0부터 시작한다고 가정)
    const [currentPage, setCurrentPage] = useState(0); 
    // 🌟 6. 다음 페이지 존재 여부 상태 (API 응답에서 받아와야 함)
    const [hasNextPage, setHasNextPage] = useState(false); 

    const PAGE_SIZE = 10;

    // 🌟 7. API 호출 로직 분리 및 useCallback으로 감싸기 (페이지 파라미터 추가)
    const fetchIssues = useCallback(async (searchKeyword, page) => {
        setLoading(true);
        setError(null);
        try {
            const response = await getGoodFirstIssues(searchKeyword, page); 
            
            const fetchedIssues = response.issues || response;
            
            // 🚨 서버 응답에서 hasNext 필드를 확인합니다.
            let hasNext = response.hasNext || false;

            // 🌟 수정된 로직: 서버에서 hasNext 필드를 정확히 주지 않을 경우, 
            // 로드된 이슈 개수가 PAGE_SIZE와 같으면 다음 페이지가 있을 것으로 가정합니다.
            if (!hasNext && fetchedIssues.length === PAGE_SIZE) { 
                hasNext = true; 
            }
            // 이슈 개수가 PAGE_SIZE보다 작으면 다음 페이지는 없다고 확정합니다.
            if (fetchedIssues.length < PAGE_SIZE) {
                 hasNext = false;
            }
            
            setIssues(fetchedIssues);
            setHasNextPage(hasNext); // 🌟 업데이트된 hasNext 값 사용

        } catch (err) {
            console.error("이슈 목록 로드 실패:", err);
            setError("이슈 목록을 불러오는 데 실패했습니다.");
            setIssues([]);
            setHasNextPage(false);
        } finally {
            setLoading(false);
        }
    }, []);

    // 8. 컴포넌트 마운트 및 페이지/키워드 변경 시 목록 로드
    // 의존성 배열에 keyword와 currentPage 추가
    useEffect(() => {
        // fetchIssues(keyword, currentPage)로 변경
        fetchIssues(keyword, currentPage); 
    }, [fetchIssues, keyword, currentPage]);


    // 9. 검색 버튼 클릭 핸들러: 검색을 실행할 때는 페이지를 0으로 리셋
    const handleSearch = () => {
        // 현재 페이지가 0이 아니면 0으로 리셋하여 useEffect에 의해 fetchIssues 호출
        // 현재 페이지가 0이면 키워드만 바뀌었으므로 바로 fetchIssues 호출
        if (currentPage !== 0) {
            setCurrentPage(0);
        } else {
            fetchIssues(keyword, 0); // 페이지가 0일 때는 검색어만 바뀌었을 가능성이 있으므로 바로 호출
        }
    };
    
    // 10. 페이지 변경 핸들러
    const handlePageChange = (page) => {
        setCurrentPage(page);
        window.scrollTo({ top: 0, behavior: 'smooth' }); 
    };

    // 11. 검색어 변경 핸들러 (SearchBar에서 호출)
    const handleKeywordChange = (e) => {
        setKeyword(e.target.value);
    };

    // 12. 로딩/에러 표시
    let content;
    if (loading) {
        content = <div className="text-center py-10">로딩 중...</div>;
    } else if (error) {
        content = <div className="text-center py-10 text-red-500">{error}</div>;
    } else if (issues.length === 0) {
        content = <div className="text-center py-10 text-gray-500 dark:text-gray-400">
            {keyword ? `'${keyword}'에 대한 검색 결과가 없습니다.` : '조회된 이슈가 없습니다.'}
        </div>;
    } else {
        content = (
            <div className="space-y-12">
                <IssueList 
                    title="Good First Issue" 
                    issues={issues} 
                />
            </div>
        );
    }
    
    return (
        <div className="min-h-screen bg-background-light dark:bg-background-dark font-display text-gray-800 dark:text-gray-200 antialiased">
            <Header />
            
            <main className="max-w-7xl mx-auto px-4 sm:px-6 lg:px-8 py-8">
                {/* SearchBar에 keyword, setKeyword, handleSearch 전달 */}
                <SearchBar 
                    keyword={keyword}
                    onKeywordChange={handleKeywordChange}
                    onSearch={handleSearch}
                />
                
                {content}

                {/* 🌟 페이지네이션 UI */}
                {!loading && issues.length > 0 && (
                    <div className="flex justify-center mt-8 space-x-4">
                        <button
                            onClick={() => handlePageChange(currentPage - 1)}
                            disabled={currentPage === 0}
                            className="px-4 py-2 border border-border-light dark:border-border-dark text-text-light-primary dark:text-text-dark-primary rounded-md disabled:opacity-50 transition-colors hover:bg-gray-100 dark:hover:bg-gray-700"
                        >
                            이전
                        </button>
                        {/* 현재 페이지 번호 (1부터 표시) */}
                        <span className="px-4 py-2 font-bold text-primary dark:text-white border border-primary dark:border-primary rounded-md bg-primary/10 dark:bg-primary/20">
                            {currentPage + 1}
                        </span>
                        <button
                            onClick={() => handlePageChange(currentPage + 1)}
                            disabled={!hasNextPage} // 다음 페이지가 없으면 비활성화
                            className="px-4 py-2 border border-border-light dark:border-border-dark text-text-light-primary dark:text-text-dark-primary rounded-md disabled:opacity-50 transition-colors hover:bg-gray-100 dark:hover:bg-gray-700"
                        >
                            다음
                        </button>
                    </div>
                )}
                
            </main>
        </div>
    );
};

export default Homepage;